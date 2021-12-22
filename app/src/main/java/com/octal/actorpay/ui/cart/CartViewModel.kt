package com.octal.actorpay.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartData
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartItemDTO
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartParams
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartUpdateParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CartViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    val cartItems= MutableStateFlow<MutableList<CartItemDTO>> (mutableListOf())
//    val cartItems= MutableLiveData<MutableList<CartItemDTO>>(mutableListOf())
    var cartData:CartData? =null


    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    fun getCartItmes() {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getCarts(token)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> {
                        //cartItems.postValue(clear())!!.
                        cartData=response.data!!.data
                        cartItems.emit(response.data.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                    }
                }
            }
        }
    }

    fun addCart(prodId:String) {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                val cartParams=CartParams(prodId)
                when (val response =
                    apiRepo.addCart(token,cartParams)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> {
                        cartItems.emit(response.data!!.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                    }
                }
            }
        }
    }
    fun deleteCart(cartId:String) {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.deleteCart(token,cartId)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> {
                        cartItems.emit(response.data!!.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                    }
                }
            }
        }
    }

    fun updateCart(cartId:String,quantity:Int) {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                val cartParams=CartUpdateParams(cartId,quantity)
                when (val response =
                    apiRepo.updateCart(token,cartParams)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> {
                        cartItems.emit(response.data!!.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                    }
                }
            }
        }
    }


}