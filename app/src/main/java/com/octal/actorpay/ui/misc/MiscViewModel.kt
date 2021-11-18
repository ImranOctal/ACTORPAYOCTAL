package com.octal.actorpay.ui.misc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.database.prefrence.SharedPre
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MiscViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository, val sharedPre: SharedPre) : AndroidViewModel(
    Application()){


    val miscResponseLive = MutableStateFlow<ResponseMiscSealed>(ResponseMiscSealed.Empty)
    sealed class ResponseMiscSealed {
        class Success(val response: Any) : ResponseMiscSealed()
        class ErrorOnResponse(val failResponse: FailResponse?) : ResponseMiscSealed()
        class loading(val isLoading: Boolean?) : ResponseMiscSealed()
        object Empty : ResponseMiscSealed()
    }

    fun changePassword(oldPassword: String,newPassword:String){
        val body= MiscChangePasswordParams(oldPassword,newPassword,newPassword)
        viewModelScope.launch(dispatcherProvider.IO){
            miscResponseLive.value= ResponseMiscSealed.loading(true)
            when(val response=apiRepo.changePassword(body,sharedPre.jwtToken!!)){
                is RetrofitResource.Error -> miscResponseLive.value =
                    ResponseMiscSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> miscResponseLive.value =
                    ResponseMiscSealed.Success(response.data!!)
            }
        }
    }
}