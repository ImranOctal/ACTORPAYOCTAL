package com.octal.actorpay.ui.dashboard.bottomnavfragments.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(
    val dispatcherProvider: CoroutineContextProvider,
    val methodRepo: MethodsRepo,
    val apiRepo: RetrofitRepository,
) : AndroidViewModel(
    Application()
) {

    val profileResponseLive = MutableStateFlow<ResponsProfileSealed>(ResponsProfileSealed.Empty)

    sealed class ResponsProfileSealed {
        class Success(val response: Any) : ResponsProfileSealed()
        class ErrorOnResponse(val failResponse: FailResponse?) : ResponsProfileSealed()
        class loading : ResponsProfileSealed()
        object Empty : ResponsProfileSealed()
    }

    fun getProfile() {

        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponsProfileSealed.loading()
            methodRepo.dataStore.getUserId().collect { id ->
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.getProfile(id, token)) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponsProfileSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponsProfileSealed.Success(response.data!!)
                    }
                }
            }

        }
    }

    fun saveProfile(email: String, extensionNumber: String, contactNumber: String) {

        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponsProfileSealed.loading()
            methodRepo.dataStore.getUserId().collect { id ->
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.saveProfile(
                        email,
                        extensionNumber,
                        contactNumber,
                        id, token
                    )) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponsProfileSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponsProfileSealed.Success(response.data!!)
                    }
                }
            }
        }
    }

    fun sendOtp() {
        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponsProfileSealed.loading()
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.sendOtp(token)) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponsProfileSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponsProfileSealed.Success(response.data!!)
                    }
                }
        }
    }
    fun verifyOtp(otp:String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponsProfileSealed.loading()
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.verifyOtp(otp,token)) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponsProfileSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponsProfileSealed.Success(response.data!!)
                    }
                }
        }
    }


}