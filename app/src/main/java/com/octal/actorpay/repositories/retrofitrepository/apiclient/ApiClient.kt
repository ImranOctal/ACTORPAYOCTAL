package com.octal.actorpay.retrofitrepository.apiclient

import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.CHANGE_PASSWORD
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.FORGETPASSWORD
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_CONTENT
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_FAQ
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_PROFILE
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.LOGIN
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.SIGNUP
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.SOCIAL_LOGIN
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.UPDATE_PROFILE
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.SocialParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignupResponse
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileParams
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileReesponse
import com.octal.actorpay.repositories.retrofitrepository.models.content.ContentResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.FAQResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST(LOGIN)
    suspend fun LoginNow(@Body loginDetail: LoginParams): Response<LoginResponses>

    @POST(SIGNUP)
    suspend fun SignUp(@Body loginDetail: SignUpParams): Response<SignupResponse>


    @POST(SOCIAL_LOGIN)
    suspend fun socialLogin(@Body socialParams: SocialParams): Response<LoginResponses>


    @POST(FORGETPASSWORD)
    suspend fun forgetPassword(@Body forgetPasswordParams: ForgetPasswordParams): Response<LoginResponses>

    @GET(GET_PROFILE + "{id}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ProfileReesponse>

    @PUT(UPDATE_PROFILE)
    suspend fun saveProfile(
        @Header("Authorization") token: String,
        @Body profileParams: ProfileParams
    ): Response<SuccessResponse>


    @POST(CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body miscChangePasswordParams: MiscChangePasswordParams
    ): Response<SuccessResponse>


    @GET(GET_CONTENT)
    suspend fun getContent(
        @Query("type") type: String
    ): Response<ContentResponse>


    @GET(GET_FAQ)
    suspend fun getFAQ(

    ): Response<FAQResponse>

}