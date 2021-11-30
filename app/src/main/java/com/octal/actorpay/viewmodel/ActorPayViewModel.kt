package com.octal.actorpay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.ui.misc.MiscViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ActorPayViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    val actorcResponseLive = MutableStateFlow<ResponseActorSealed>(ResponseActorSealed.Empty)
    sealed class ResponseActorSealed {
        class Success(val response: Any) : ResponseActorSealed()
        class ErrorOnResponse(val failResponse: FailResponse?) : ResponseActorSealed()
        class loading : ResponseActorSealed()
        object Empty : ResponseActorSealed()
    }

    fun changePassword(oldPassword: String,newPassword:String){
        val body= MiscChangePasswordParams(oldPassword,newPassword,newPassword)
        viewModelScope.launch(dispatcherProvider.IO){
            actorcResponseLive.value= ResponseActorSealed.loading()
            methodRepo.dataStore.getAccessToken().collect { token ->
                when(val response=apiRepo.changePassword(body,token)){
                    is RetrofitResource.Error -> actorcResponseLive.value =
                        ResponseActorSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> actorcResponseLive.value =
                        ResponseActorSealed.Success(response.data!!)
                }
            }
        }
    }

}