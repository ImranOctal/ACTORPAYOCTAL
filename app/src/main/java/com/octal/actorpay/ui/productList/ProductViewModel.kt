package com.octal.actorpay.ui.productList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpay.repositories.retrofitrepository.models.categories.CategorieItem
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductData
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductItem
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

    val productList:MutableList<ProductItem> = mutableListOf()

    val responseLive = MutableStateFlow<ResponseSealed>(ResponseSealed.Empty)

    var productData=ProductData(0,0, mutableListOf(),0,10)
    var categoryList= mutableListOf<CategorieItem>()

    var name=""
    var category=""
    fun getProducts() {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getProducts(token,productData.pageNumber, productData.pageSize,
                        ProductParams(category,name)
                    )) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> responseLive.value =
                        ResponseSealed.Success(response.data!!)
                }
            }
        }
    }

    fun getCategories() {

        viewModelScope.launch(dispatcherProvider.IO) {
            responseLive.value = ResponseSealed.loading(true)
            methodRepo.dataStore.getAccessToken().collect { token ->
                when (val response =
                    apiRepo.getCategories(token)) {
                    is RetrofitResource.Error -> responseLive.value =
                        ResponseSealed.ErrorOnResponse(response.message)
                    is RetrofitResource.Success -> responseLive.value =
                        ResponseSealed.Success(response.data!!)
                }
            }
        }
    }




}