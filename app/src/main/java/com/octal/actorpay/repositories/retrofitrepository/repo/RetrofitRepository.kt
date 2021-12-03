package com.octal.actorpay.repositories.retrofitrepository.repo

import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.SocialParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignupResponse
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileReesponse
import com.octal.actorpay.repositories.retrofitrepository.models.content.ContentResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.FAQResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource

/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */


interface RetrofitRepository {
    //Login Suspend Function
    suspend fun LoginNow(loginDetail: LoginParams): RetrofitResource<LoginResponses>

    suspend fun SignUpNow(signupDetails:SignUpParams):RetrofitResource<SignupResponse>

    suspend fun socialLogin(signupDetails:SocialParams):RetrofitResource<LoginResponses>

    suspend fun ForgetPassword(forgetPasswordParams:ForgetPasswordParams):RetrofitResource<LoginResponses>

    suspend fun getProfile(id:String,token:String):RetrofitResource<ProfileReesponse>

    suspend fun saveProfile(email:String,extensionNumber:String,contactNumber:String,id:String,token: String):RetrofitResource<SuccessResponse>

    suspend fun changePassword(miscChangePasswordParams: MiscChangePasswordParams,token: String):RetrofitResource<SuccessResponse>

    suspend fun getContent(type:String):RetrofitResource<ContentResponse>

    suspend fun getFAQ():RetrofitResource<FAQResponse>



}