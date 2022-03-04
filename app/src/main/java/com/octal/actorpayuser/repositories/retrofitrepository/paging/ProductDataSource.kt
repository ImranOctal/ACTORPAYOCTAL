package com.octal.actorpayuser.repositories.retrofitrepository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.retrofitrepository.apiclient.ApiClient
import com.octal.actorpayuser.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductParams
import org.json.JSONObject

class ProductDataSource(private var apiClient: ApiClient,val token: String,
                     val productParams: ProductParams
): PagingSource<Int, ProductItem>()
{
    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        val pageNumber = params.key ?: 0
        return try {
            val response = apiClient.getProducts(AppConstance.B_Token + token,pageNumber,10, productParams)

            if(response.isSuccessful.not()) {
                return LoadResult.Error(
                    Throwable(
                        handleError(
                            response.code(),
                            response.errorBody()!!.string()
                        )
                    )
                )

            }
            val pagedResponse = response.body()!!
            val data = pagedResponse.data


            var nextPageNumber: Int? = null
            if (pagedResponse.data.pageNumber < (pagedResponse.data.totalPages-1)) {

                nextPageNumber =pagedResponse.data.pageNumber+1
            }

            LoadResult.Page(
                data = data.items,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    fun handleError(code:Int,error:String): String {

        if (code == 403) {
            val json = JSONObject()
            json.put("code", code)
            return json.toString()
        }
       else {
            val json = JSONObject(error)
            json.put("code", 0)
            return json.toString()
        }

    }


    }



