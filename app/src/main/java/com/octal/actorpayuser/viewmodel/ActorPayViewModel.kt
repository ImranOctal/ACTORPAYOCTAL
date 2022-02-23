package com.octal.actorpayuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ActorPayViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    val actorResponseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)


    fun getAllCountries(){
        viewModelScope.launch(dispatcherProvider.IO){
            actorResponseLive.value= ResponseSealed.loading(true)
            when(val response=apiRepo.getAllCountries()){
                is RetrofitResource.Error ->{
                    actorResponseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    this.cancel()
                }
                is RetrofitResource.Success ->{
                    actorResponseLive.value =
                        ResponseSealed.Success(response.data!!)
                    this.cancel()
                }
            }
        }
    }
}