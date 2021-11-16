package com.octal.actorpay.repositories.retrofitrepository.repo
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */

import android.content.Context
import com.octal.actorpay.R
import com.octal.actorpay.repositories.retrofitrepository.models.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.retrofitrepository.apiclient.ApiClient
import org.json.JSONObject

class RetrofitMainRepository constructor(var context: Context, private var apiClient: ApiClient) :
    RetrofitRepository {
    override suspend fun LoginNow(loginDetail: JSONObject): RetrofitResource<LoginResponses> {
        return try {
            val loginData = apiClient.LoginNow(loginDetail)
            val result = loginData.body()
            if (loginData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                return RetrofitResource.Error(context.getString(R.string.error_on_fetching_loginDetails))
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(e.message ?: context.getString(R.string.server_not_responding))
        }
    }

}