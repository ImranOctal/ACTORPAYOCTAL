package com.octal.actorpay.ui.misc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.FAQResponseData
import com.octal.actorpay.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MiscViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository) : AndroidViewModel(
    Application()){

    val faqList= mutableListOf<FAQResponseData>()
    val miscResponseLive = MutableStateFlow<ResponseMiscSealed>(ResponseMiscSealed.Empty)
    sealed class ResponseMiscSealed {
        class Success(val response: Any) : ResponseMiscSealed()
        class ErrorOnResponse(val failResponse: FailResponse?) : ResponseMiscSealed()
        class loading : ResponseMiscSealed()
        object Empty : ResponseMiscSealed()
    }

    fun getFAQ(){

        viewModelScope.launch(dispatcherProvider.IO){
            miscResponseLive.value= ResponseMiscSealed.loading()
            methodRepo.dataStore.getAccessToken().collect { token ->
                when(val response=apiRepo.getFAQ()){
                    is RetrofitResource.Error -> miscResponseLive.value =
                        ResponseMiscSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> {
                        faqList.clear()
                        faqList.addAll(response.data!!.data)
                        miscResponseLive.value =
                        ResponseMiscSealed.Success(response.data)
                    }
                }
            }
        }
    }
}