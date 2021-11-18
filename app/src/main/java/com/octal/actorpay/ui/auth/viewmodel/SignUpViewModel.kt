package com.octal.actorpay.ui.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.database.prefrence.SharedPre
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository, val sharedPre: SharedPre):
    AndroidViewModel(Application())
{
    val signInResponseLive = MutableStateFlow<ResponseSignupSealed>(ResponseSignupSealed.Empty)
    sealed class ResponseSignupSealed {
        class Success(val response: String) : ResponseSignupSealed()
        class ErrorOnResponse(val message: FailResponse?) : ResponseSignupSealed()
        class loading(val isLoading: Boolean?) : ResponseSignupSealed()
        object Empty : ResponseSignupSealed()
    }

    fun SignUpNow(firstName:String,lastName:String,email: String,extensionNumber:String,contactNumber:String, password: String,) {
        val body= SignUpParams(firstName, lastName, email, extensionNumber, contactNumber, password)
        viewModelScope.launch(dispatcherProvider.IO) {
            signInResponseLive.value = ResponseSignupSealed.loading(true)
            when (val response = apiRepo.SignUpNow(body)) {
                is RetrofitResource.Error -> signInResponseLive.value =
                    ResponseSignupSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> signInResponseLive.value =
                    ResponseSignupSealed.Success(response.data!!.message)
            }
        }
    }
}
