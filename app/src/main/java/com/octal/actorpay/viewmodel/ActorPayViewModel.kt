package com.octal.actorpay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
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
                is RetrofitResource.Error -> actorResponseLive.value =
                    ResponseSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> actorResponseLive.value =
                    ResponseSealed.Success(response.data!!)
            }
        }
    }
}