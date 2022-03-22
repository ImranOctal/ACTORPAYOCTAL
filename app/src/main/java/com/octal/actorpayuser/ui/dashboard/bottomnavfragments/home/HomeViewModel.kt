package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderListParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.AddMoneyParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletListData
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WallletMoneyParams
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    var walletListData = WalletListData(0, 0, mutableListOf(), 0, 5)
    var walletParams= WallletMoneyParams()

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    fun getWalletBalance() {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                methodRepo.dataStore.getUserId().collect { id ->
                    when (val response = apiRepo.getWalletBalance(token, id)) {
                        is RetrofitResource.Error -> {
                            responseLive.value =
                                ResponseSealed.ErrorOnResponse(response.message)
//                            this.cancel()
                        }
                        is RetrofitResource.Success -> {
                            responseLive.value =
                                ResponseSealed.Success(response.data!!)
//                            this.cancel()
                        }
                    }
                }
            }
        }

    }

    fun getWalletHistory() {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(false)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getWalletHistory(token,walletListData.pageNumber,walletListData.pageSize,
                        walletParams
                    )) {
                    is RetrofitResource.Error ->{
                        responseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
//                        this.cancel()
                    }
                    is RetrofitResource.Success -> {
                        responseLive.value =
                            ResponseSealed.Success(response.data!!)
//                        this.cancel()
                    }
                }
            }
        }
    }

    fun getGlobalData() {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(false)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getGlobalData(token)) {
                    is RetrofitResource.Error ->{
                        responseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
//                        this.cancel()
                    }
                    is RetrofitResource.Success -> {
                        responseLive.value =
                            ResponseSealed.Success(response.data!!)
//                        this.cancel()
                    }
                }
            }
        }
    }


}