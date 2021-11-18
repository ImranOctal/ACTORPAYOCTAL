package com.octal.actorpay.repositories.retrofitrepository.repo
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */

import android.content.Context
import com.octal.actorpay.R
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.B_Token
import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.SocialParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignupResponse
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileParams
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.retrofitrepository.apiclient.ApiClient
import org.json.JSONException
import org.json.JSONObject

class RetrofitMainRepository constructor(var context: Context, private var apiClient: ApiClient) :
    RetrofitRepository {
    override suspend fun LoginNow(loginDetail: LoginParams): RetrofitResource<LoginResponses> {
        try {
            val loginData = apiClient.LoginNow(loginDetail)
            val result = loginData.body()
            if (loginData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(loginData.errorBody()!=null) {
                    val json=JSONObject(loginData.errorBody()!!.string())
                    val status=json.getString("status")
                    val message=json.getString("message")
                    return RetrofitResource.Error(FailResponse(message, status))
                }
                return RetrofitResource.Error(FailResponse(context.getString(R.string.please_try_after_sometime),""))
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(FailResponse(e.message ?: context.getString(R.string.server_not_responding),""))
        }
    }

    override suspend fun SignUpNow(signupDetails: SignUpParams): RetrofitResource<SignupResponse> {
        try {
            val signupData = apiClient.SignUp(signupDetails)
            val result = signupData.body()
            if (signupData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(signupData.errorBody()!=null) {
                    val json=JSONObject(signupData.errorBody()!!.string())
                    val status=json.getString("status")
                    val message=json.getString("message")
                    return RetrofitResource.Error(FailResponse(message, status))
                }
                return RetrofitResource.Error(FailResponse(context.getString(R.string.please_try_after_sometime),""))
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(FailResponse(e.message ?: context.getString(R.string.server_not_responding),""))
        }
    }

    override suspend fun socialLogin(socialParams: SocialParams): RetrofitResource<LoginResponses> {
        try {
            val loginData = apiClient.socialLogin(socialParams)
            val result = loginData.body()
            if (loginData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(loginData.errorBody()!=null) {
                    val json=JSONObject(loginData.errorBody()!!.string())
                    val status=json.getString("status")
                    val message=json.getString("message")
                    return RetrofitResource.Error(FailResponse(message, status))
                }
                return RetrofitResource.Error(FailResponse(context.getString(R.string.please_try_after_sometime),""))
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(FailResponse(e.message ?: context.getString(R.string.server_not_responding),""))
        }
    }

    override suspend fun ForgetPassword(forgetPasswordParams: ForgetPasswordParams): RetrofitResource<LoginResponses> {
        try {
            val forgetData = apiClient.forgetPassword(forgetPasswordParams)
            val result = forgetData.body()
            if (forgetData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(forgetData.errorBody()!=null) {
                    val json=JSONObject(forgetData.errorBody()!!.string())
                    val status=json.getString("status")
                    val message=json.getString("message")
                    return RetrofitResource.Error(FailResponse(message, status))
                }
                return RetrofitResource.Error(FailResponse(context.getString(R.string.please_try_after_sometime),""))
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(FailResponse(e.message ?: context.getString(R.string.server_not_responding),""))
        }
    }

    override suspend fun getProfile(id: String, token: String): RetrofitResource<SuccessResponse> {
        try {

            val data = apiClient.getProfile(B_Token+token,id)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(data.errorBody()!=null) {
                    val json=JSONObject(data.errorBody()!!.string())
                    val status=json.getString("status")
                    val message=json.getString("message")
                    return RetrofitResource.Error(FailResponse(message, status))
                }
                return RetrofitResource.Error(FailResponse(context.getString(R.string.please_try_after_sometime),""))
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(FailResponse(e.message ?: context.getString(R.string.server_not_responding),""))
        }
    }

    override suspend fun saveProfile(email:String,extensionNumber:String,contactNumber:String,id:String,token: String): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.saveProfile(B_Token+token,ProfileParams(email, extensionNumber, contactNumber, id))
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(data.errorBody()!=null) {
                    val json=JSONObject(data.errorBody()!!.string())
                    val status=json.getString("status")
                    val message=json.getString("message")
                    return RetrofitResource.Error(FailResponse( message, status))
                }
                return RetrofitResource.Error(FailResponse(context.getString(R.string.please_try_after_sometime),""))
            }
        }
        catch (e: Exception) {
            return RetrofitResource.Error(FailResponse(e.message ?: context.getString(R.string.server_not_responding),""))
        }
    }

    override suspend fun changePassword(miscChangePasswordParams: MiscChangePasswordParams,token: String): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.changePassword(B_Token+token,miscChangePasswordParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(data.errorBody()!=null) {
                    val json=JSONObject(data.errorBody()!!.string())
                    val status=json.getString("status")
                    val message=json.getString("message")
                    return RetrofitResource.Error(FailResponse( message, status))
                }
                return RetrofitResource.Error(FailResponse(context.getString(R.string.please_try_after_sometime),""))
            }
        }
        catch (e: Exception) {
            return RetrofitResource.Error(FailResponse(e.message ?: context.getString(R.string.server_not_responding),""))
        }
    }


}