package com.octal.actorpay.retrofitrepository.apiclient

import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.LOGIN
import com.octal.actorpay.repositories.retrofitrepository.models.login.LoginResponses
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST(LOGIN)
    suspend fun LoginNow(loginDetail:JSONObject):Response<LoginResponses>


}