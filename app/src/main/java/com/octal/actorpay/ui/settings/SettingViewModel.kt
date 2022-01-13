package com.octal.actorpay.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.base.ResponseSealed
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

class SettingViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository) : AndroidViewModel(
    Application()){

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    fun changePassword(oldPassword: String,newPassword:String){
        val body= MiscChangePasswordParams(oldPassword,newPassword,newPassword)
        viewModelScope.launch(dispatcherProvider.IO){
            responseLive.value= ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when(val response=apiRepo.changePassword(body,token)){
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> responseLive.value =
                        ResponseSealed.Success(response.data!!)
                }
            }
        }
    }
}