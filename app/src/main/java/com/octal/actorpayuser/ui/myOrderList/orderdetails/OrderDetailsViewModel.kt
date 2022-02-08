package com.octal.actorpayuser.ui.myOrderList.orderdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.AddNoteParam
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderData
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class OrderDetailsViewModel(
    val dispatcherProvider: CoroutineContextProvider,
    val methodRepo: MethodsRepo,
    val apiRepo: RetrofitRepository
) : AndroidViewModel(
    Application()
) {

    var orderData:OrderData? =null
    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    fun changeOrderItemsStatus(orderNo:String,cancelOrder: String,file: File?) {

        var r1: RequestBody? = null
        var f1: MultipartBody.Part? = null
        if(file!=null) {
            r1 = file.asRequestBody("/*".toMediaTypeOrNull())
            f1 =
                MultipartBody.Part.createFormData(
                    "file",
                    "${System.currentTimeMillis()}.jpg",
                    r1
                )
        }
        val prod = cancelOrder.toRequestBody("application/json".toMediaTypeOrNull())

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.changeOrderItemsStatus(token, orderNo,prod,f1)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> {
                        responseLive.value =
                            ResponseSealed.Success(response.data!!)
                    }
                }
            }
        }
    }

    fun getOrder(orderNo: String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getOrder(token,orderNo)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> {
                        responseLive.value =
                            ResponseSealed.Success(response.data!!)
                    }
                }
            }
        }
    }

    fun addNote(note: String, orderNo: String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            val body= AddNoteParam(note,orderNo)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response = apiRepo.addNote(token,body)) {
                    is RetrofitResource.Error -> responseLive.value = ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> responseLive.value =
                        ResponseSealed.Success(response.data!!)
                }
            }
        }
    }


}