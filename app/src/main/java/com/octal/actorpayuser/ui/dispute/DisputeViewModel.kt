package com.octal.actorpayuser.ui.dispute

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.DisputeListData
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.DisputeListParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderListData
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderListParams
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DisputeViewModel (val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    var disputeListData = DisputeListData(0, 0, mutableListOf(), 0, 20)
    var disputeListParams= DisputeListParams("","","","")

    fun getAllDisputes() {
        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getAllDisputes(token,disputeListData.pageNumber,disputeListData.pageSize,
                        disputeListParams
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