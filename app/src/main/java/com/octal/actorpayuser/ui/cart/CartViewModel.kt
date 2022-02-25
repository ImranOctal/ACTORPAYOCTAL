package com.octal.actorpayuser.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartData
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartItemDTO
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartUpdateParams
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    val cartItems= MutableStateFlow<MutableList<CartItemDTO>> (mutableListOf())
    var cartData:CartData? =null


    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    fun getCartItems() {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->

                when (val response =
                    apiRepo.getCarts(token)) {
                    is RetrofitResource.Error -> {

                        responseLive.value = ResponseSealed.ErrorOnResponse(response.message)
                        this.cancel()
                    }
                    is RetrofitResource.Success -> {


                        cartData=response.data!!.data
                        cartItems.emit(response.data.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                        this.cancel()
                    }
                }
            }
        }
    }

    fun addCart(prodId:String,price:Double) {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                val cartParams=CartParams(prodId,price)
                when (val response =
                    apiRepo.addCart(token,cartParams)) {
                    is RetrofitResource.Error ->
                    {
                        responseLive.value = ResponseSealed.ErrorOnResponse(response.message)
                        this.cancel()
                    }
                    is RetrofitResource.Success -> {
                        cartItems.emit(response.data!!.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                        this.cancel()
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
                    is RetrofitResource.Error ->{
                        responseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        this.cancel()
                    }
                    is RetrofitResource.Success -> {
                        cartItems.emit(response.data!!.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                        this.cancel()
                    }
                }
            }
        }
    }

    fun updateCart(cartId:String,quantity:Int) {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(false)
            methodRepo.dataStore.getAccessToken().collect { token ->
                val cartParams=CartUpdateParams(cartId,quantity)
                when (val response =
                    apiRepo.updateCart(token,cartParams)) {
                    is RetrofitResource.Error -> {
                        responseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        this.cancel()
                    }
                    is RetrofitResource.Success -> {
                        cartItems.emit(response.data!!.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                        this.cancel()
                    }
                }
            }
        }
    }


    fun deleteAllCart() {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.deleteAllCart(token)) {
                    is RetrofitResource.Error -> {
                        responseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        this.cancel()
                    }
                    is RetrofitResource.Success -> {
                        cartItems.emit(response.data!!.data.cartItemDTOList)
                        responseLive.value =
                            ResponseSealed.Success(response.data)
                        this.cancel()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}