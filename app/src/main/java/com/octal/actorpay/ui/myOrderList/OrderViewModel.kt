package com.octal.actorpay.ui.myOrderList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListData
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OrderViewModel(
    val dispatcherProvider: CoroutineContextProvider,
    val methodRepo: MethodsRepo,
    val apiRepo: RetrofitRepository
) : AndroidViewModel(
    Application()
) {

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    var orderListData = OrderListData(0, 0, mutableListOf(), 0, 10)
    var orderStatus=""
    var orderListParams=OrderListParams()

    fun getAllOrders(orderListParams: OrderListParams) {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getAllOrders(token,orderListData.pageNumber,orderListData.pageSize,
                        orderListParams
                    )) {
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

}