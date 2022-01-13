package com.octal.actorpay.ui.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.BuildConfig
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.DeviceInfoParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignupViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository):
    AndroidViewModel(Application()) {

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    fun SignUpNow(
        firstName: String,
        lastName: String,
        email: String,
        extensionNumber: String,
        contactNumber: String,
        password: String,
        gender: String,
        dob: String,
        adhar: String,
        pan: String
    ) {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getDeviceToken().collect { deviceToken ->
                val deviceInfo = DeviceInfoParams(
                    "Android",
                    BuildConfig.VERSION_CODE.toString(),
                    deviceToken,
                    ""
                )
                val body = SignUpParams(
                    firstName,
                    lastName,
                    email,
                    extensionNumber,
                    contactNumber,
                    password,
                    gender,
                    dob,
                    pan,
                    adhar,
                    deviceInfo
                )

                when (val response = apiRepo.signUpNow(body)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> responseLive.value =
                        ResponseSealed.Success(response.data!!)
                }
            }
        }
    }
}
