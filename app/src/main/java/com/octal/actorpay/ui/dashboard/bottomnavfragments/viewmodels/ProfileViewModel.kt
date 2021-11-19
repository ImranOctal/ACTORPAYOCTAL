package com.octal.actorpay.ui.dashboard.bottomnavfragments.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.database.prefrence.SharedPre
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileReesponse
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository, val sharedPre: SharedPre) : AndroidViewModel(
    Application()){

    val profileResponseLive = MutableStateFlow<ResponsProfileSealed>(ResponsProfileSealed.Empty)
    sealed class ResponsProfileSealed {
        class Success(val response: Any) : ResponsProfileSealed()
        class ErrorOnResponse(val failResponse: FailResponse?) : ResponsProfileSealed()
        class loading(val isLoading: Boolean?) : ResponsProfileSealed()
        object Empty : ResponsProfileSealed()
    }

    fun getProfile() {

        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponsProfileSealed.loading(true)
            when (val response = apiRepo.getProfile(sharedPre.userId!!,sharedPre.jwtToken!!)) {
                is RetrofitResource.Error -> profileResponseLive.value =
                    ResponsProfileSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> profileResponseLive.value =
                    ResponsProfileSealed.Success(response.data!!)
            }
        }
    }

    fun saveProfile(email:String,extensionNumber:String,contactNumber:String) {

        viewModelScope.launch(dispatcherProvider.IO) {
            profileResponseLive.value = ResponsProfileSealed.loading(true)
            when (val response = apiRepo.saveProfile(email,extensionNumber,contactNumber,sharedPre.userId!!,sharedPre.jwtToken!!)) {
                is RetrofitResource.Error -> profileResponseLive.value =
                    ResponsProfileSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> profileResponseLive.value =
                    ResponsProfileSealed.Success(response.data!!)
            }
        }
    }


}