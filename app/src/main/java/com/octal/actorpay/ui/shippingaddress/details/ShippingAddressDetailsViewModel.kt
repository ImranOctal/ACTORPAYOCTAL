package com.octal.actorpay.ui.shippingaddress.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShippingAddressDetailsViewModel(val dispatcherProvider: CoroutineContextProvider,
                                      val methodRepo: MethodsRepo,
                                      val apiRepo: RetrofitRepository,
) : AndroidViewModel(
    Application()
) {

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    fun addAddress(shippingAddressItem: ShippingAddressItem) {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.addAddress(token,shippingAddressItem)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> responseLive.value =
                        ResponseSealed.Success(response.data!!)
                }
            }
        }
    }

    fun updateAddress(shippingAddressItem: ShippingAddressItem) {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.updateAddress(token,shippingAddressItem)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> responseLive.value =
                        ResponseSealed.Success(response.data!!)
                }
            }
        }
    }
}