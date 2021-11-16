package com.octal.actorpay.repositories.retrofitrepository.repo

import com.octal.actorpay.repositories.retrofitrepository.models.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET

/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */


interface RetrofitRepository {
    //Login Suspend Function
    suspend fun LoginNow(loginDetail: JSONObject): RetrofitResource<LoginResponses>




}