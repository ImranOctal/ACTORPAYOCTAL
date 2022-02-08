package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
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

    val profileResponseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)



    fun getProfile() {

        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getUserId().collect { id ->
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.getProfile(id, token)) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponseSealed.Success(response.data!!)
                    }
                }
            }

        }
    }

    fun saveProfile(email: String, extensionNumber: String, contactNumber: String) {

        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getUserId().collect { id ->
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.saveProfile(
                        email,
                        extensionNumber,
                        contactNumber,
                        id, token
                    )) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponseSealed.Success(response.data!!)
                    }
                }
            }
        }
    }

    fun sendOtp() {
        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponseSealed.loading(true)
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.sendOtp(token)) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponseSealed.Success(response.data!!)
                    }
                }
        }
    }
    fun verifyOtp(otp:String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponseSealed.loading(true)
                methodRepo.dataStore.getAccessToken().collect { token ->
                    when (val response = apiRepo.verifyOtp(otp,token)) {
                        is RetrofitResource.Error -> profileResponseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        is RetrofitResource.Success -> profileResponseLive.value =
                            ResponseSealed.Success(response.data!!)
                    }
                }
        }
    }


}