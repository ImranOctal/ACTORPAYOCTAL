package com.octal.actorpay.ui.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.BuildConfig
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.*
import kotlinx.coroutines.flow.collect


class LoginViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {
    companion object{
    var isFromContentPage=false
    }
    val loginResponseLive = MutableStateFlow<ResponseLoginSealed>(ResponseLoginSealed.Empty)
    sealed class ResponseLoginSealed {
        class Success(val response: Any) : ResponseLoginSealed()
        class ErrorOnResponse(val message: FailResponse?) : ResponseLoginSealed()
        class loading(val isLoading: Boolean?) : ResponseLoginSealed()
        object Empty : ResponseLoginSealed()
    }
    fun SignInNow(email: String, password: String) {

        viewModelScope.launch(dispatcherProvider.IO) {
            methodRepo.dataStore.getDeviceToken().collect { deviceToken ->
                val deviceInfo = DeviceInfoParams("Android", BuildConfig.VERSION_CODE.toString(), deviceToken, "")
                val body = LoginParams(email, password, deviceInfo)
                loginResponseLive.value = ResponseLoginSealed.loading(true)
                when (val response = apiRepo.LoginNow(body)) {
                    is RetrofitResource.Error -> loginResponseLive.value =
                        ResponseLoginSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> loginResponseLive.value =
                        ResponseLoginSealed.Success(response.data!!)
                }
            }
        }
    }


    fun socialLogin(firstName:String,lastName:String,email:String,socialId:String,imgUrl:String) {

        viewModelScope.launch(dispatcherProvider.IO) {
            loginResponseLive.value = ResponseLoginSealed.loading(true)
            methodRepo.dataStore.getDeviceToken().collect { deviceToken ->
                val deviceInfo = DeviceInfoParams(
                    "Android",
                    BuildConfig.VERSION_CODE.toString(),
                    deviceToken,
                    ""
                )
                val body=SocialParams(firstName,lastName,email,socialId,imgUrl,deviceInfo)

                when (val response = apiRepo.socialLogin(body)) {
                    is RetrofitResource.Error -> loginResponseLive.value =
                        ResponseLoginSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> loginResponseLive.value =
                        ResponseLoginSealed.Success(response.data!!)
                }
            }
        }
    }


    fun forgetPassword(email: String){
        val body=ForgetPasswordParams(email)
        viewModelScope.launch(dispatcherProvider.IO){
            loginResponseLive.value=ResponseLoginSealed.loading(true)
            when(val response=apiRepo.ForgetPassword(body)){
                is RetrofitResource.Error -> loginResponseLive.value =
                    ResponseLoginSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> loginResponseLive.value =
                    ResponseLoginSealed.Success(response.data!!.message)
            }
        }
    }

    fun resendOtp(email: String){
        val body=ForgetPasswordParams(email)
        viewModelScope.launch(dispatcherProvider.IO){
            loginResponseLive.value=ResponseLoginSealed.loading(true)
            when(val response=apiRepo.resendOtp(body)){
                is RetrofitResource.Error -> loginResponseLive.value =
                    ResponseLoginSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> loginResponseLive.value =
                    ResponseLoginSealed.Success(response.data!!.message)
            }
        }
    }


}