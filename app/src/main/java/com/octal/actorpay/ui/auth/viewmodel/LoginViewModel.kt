package com.octal.actorpay.ui.auth.viewmodel

import android.R.attr
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONException

import android.R.attr.password
import com.octal.actorpay.database.prefrence.SharedPre

import org.json.JSONObject




class LoginViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository,val sharedPre:SharedPre)  : AndroidViewModel(
    Application()
) {
    val loginResponseLive = MutableStateFlow<ResponseLoginSealed>(ResponseLoginSealed.Empty)
    sealed class ResponseLoginSealed {
        class Success(val response: LoginResponses) : ResponseLoginSealed()
        class ErrorOnResponse(val message: String?) : ResponseLoginSealed()
        class loading(val isLoading: Boolean?) : ResponseLoginSealed()
        object Empty : ResponseLoginSealed()
    }
    fun SignInNow(email: String, password: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("email", email)
            jsonObject.put("password", password)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        viewModelScope.launch(dispatcherProvider.IO) {
            loginResponseLive.value = ResponseLoginSealed.loading(true)
            when (val response = apiRepo.LoginNow(jsonObject)) {
                is RetrofitResource.Error -> loginResponseLive.value =
                    ResponseLoginSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> loginResponseLive.value =
                    ResponseLoginSealed.Success(response.data!!)
            }
        }
    }
}