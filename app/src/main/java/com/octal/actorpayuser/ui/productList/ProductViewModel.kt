package com.octal.actorpayuser.ui.productList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductData
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch

class ProductViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    val productList:MutableList<ProductItem> = mutableListOf()

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    var pageNumber=0
    var categoryList= mutableListOf<CategorieItem>()
    var name=""
    var category=""



  suspend fun getProductsWithPaging(token:String)=
      apiRepo.getProductsWithPaging(
          viewModelScope, token,
          ProductParams(category, name)
      )



    fun getCategories() {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getCategories(token)) {
                    is RetrofitResource.Error ->{
                        responseLive.value =
                            ResponseSealed.ErrorOnResponse(response.message)
                        this.cancel()
                    }
                    is RetrofitResource.Success ->{
                        responseLive.value =
                            ResponseSealed.Success(response.data!!)
                        this.cancel()
                    }
                }
            }
        }
    }




}