package com.octal.actorpay.ui.misc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.misc.FAQResponseData
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MiscViewModel(
    val dispatcherProvider: CoroutineContextProvider,
    val methodRepo: MethodsRepo,
    val apiRepo: RetrofitRepository
) : AndroidViewModel(
    Application()
) {

    val faqList = mutableListOf<FAQResponseData>()
    val miscResponseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)


    fun getFAQ() {

        viewModelScope.launch(dispatcherProvider.IO) {
            miscResponseLive.value = ResponseSealed.loading(true)
            when (val response = apiRepo.getFAQ()) {
                is RetrofitResource.Error -> miscResponseLive.value =
                    ResponseSealed.ErrorOnResponse(response.message)
                is RetrofitResource.Success -> {
                    faqList.clear()
                    faqList.addAll(response.data!!.data)
                    miscResponseLive.value =
                        ResponseSealed.Success(response.data)
                }
            }
        }
    }
}