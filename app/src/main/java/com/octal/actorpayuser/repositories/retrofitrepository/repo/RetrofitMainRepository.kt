package com.octal.actorpayuser.repositories.retrofitrepository.repo
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */

import android.content.Context
import com.octal.actorpayuser.R
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.B_Token
import com.octal.actorpayuser.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.SocialParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.signup.SignupResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.bottomfragments.ProfileParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.bottomfragments.ProfileResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartUpdateParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.SubCategorieResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.content.ContentResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.CountryResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.FAQResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.promocodes.PromoResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpayuser.repositories.retrofitrepository.apiclient.ApiClient
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.*
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.*
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.AddMoneyParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.TransferMoneyParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletHistoryResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WallletMoneyParams
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitMainRepository constructor(var context: Context, private var apiClient: ApiClient) :
    RetrofitRepository {
    override suspend fun loginNow(loginDetail: LoginParams): RetrofitResource<LoginResponses> {
        try {
            val loginData = apiClient.loginNow(loginDetail)
            val result = loginData.body()
            if (loginData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (loginData.errorBody() != null) {
                    return RetrofitResource.Error(handleError(loginData.code(),loginData.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun signUpNow(signupDetails: SignUpParams): RetrofitResource<SignupResponse> {
        try {
            val signupData = apiClient.signUp(signupDetails)
            val result = signupData.body()
            if (signupData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (signupData.errorBody() != null) {
                    return RetrofitResource.Error(handleError(signupData.code(),signupData.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun socialLogin(signupDetails: SocialParams): RetrofitResource<LoginResponses> {
        try {
            val loginData = apiClient.socialLogin(signupDetails)
            val result = loginData.body()
            if (loginData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (loginData.errorBody() != null) {
                    return RetrofitResource.Error(handleError(loginData.code(),loginData.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun forgetPassword(forgetPasswordParams: ForgetPasswordParams): RetrofitResource<LoginResponses> {
        try {
            val forgetData = apiClient.forgetPassword(forgetPasswordParams)
            val result = forgetData.body()
            if (forgetData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (forgetData.errorBody() != null) {
                    return RetrofitResource.Error(handleError(forgetData.code(),forgetData.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getProfile(id: String, token: String): RetrofitResource<ProfileResponse> {
        try {

            val data = apiClient.getProfile(B_Token + token, id)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun saveProfile(
        email: String,
        extensionNumber: String,
        contactNumber: String,
        id: String,
        token: String
    ): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.saveProfile(
                B_Token + token,
                ProfileParams(email, extensionNumber, contactNumber, id)
            )
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun sendOtp(token: String): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.sendOtp(B_Token + token)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun verifyOtp(otp: String, token: String): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.verifyOtp(
                B_Token + token,otp)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun changePassword(
        miscChangePasswordParams: MiscChangePasswordParams,
        token: String
    ): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.changePassword(B_Token + token, miscChangePasswordParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getContent(type: String): RetrofitResource<ContentResponse> {

        try {

            val data = apiClient.getContent(type)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getFAQ(): RetrofitResource<FAQResponse> {

        try {

            val data = apiClient.getFAQ()
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }


    override suspend fun getProducts(
        token: String,
        pageNo: Int,
        pageSize: Int,
        productParams: ProductParams
    ): RetrofitResource<ProductListResponse> {

        try {

            val data = apiClient.getProducts(B_Token + token, pageNo, pageSize, productParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), "",
                )
            )
        }
    }

    override suspend fun getProductById(
        token: String,
        id: String
    ): RetrofitResource<SingleProductResponse> {

        try {

            val data = apiClient.getProductById(B_Token + token,id)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), "",
                )
            )
        }
    }


    override suspend fun getCarts(token: String): RetrofitResource<CartResponse> {

        try {

            val data = apiClient.getCarts(B_Token + token)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun addCart(
        token: String,
        cartParams: CartParams
    ): RetrofitResource<CartResponse> {

        try {

            val data = apiClient.addCart(B_Token + token, cartParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }


    override suspend fun deleteCart(token: String, cartId: String): RetrofitResource<CartResponse> {

        try {

            val data = apiClient.deleteCart(B_Token + token, cartId)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun deleteAllCart(token: String): RetrofitResource<CartResponse> {

        try {

            val data = apiClient.deleteAllCart(B_Token + token)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun updateCart(
        token: String,
        cartParams: CartUpdateParams
    ): RetrofitResource<CartResponse> {

        try {

            val data = apiClient.updateCart(B_Token + token, cartParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }


    override suspend fun placeOrder(token: String,placeOrderParams: PlaceOrderParams): RetrofitResource<PlaceOrderResponse> {

        try {
            val data = apiClient.placeOrder(B_Token + token,placeOrderParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getAllOrders(token: String,pageNo:Int,pageSize:Int,orderListParams: OrderListParams): RetrofitResource<OrderListResponse> {

        try {
            val data = apiClient.getAllOrders(B_Token + token,pageNo, pageSize, orderListParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getOrder(
        token: String,
        orderNo: String
    ): RetrofitResource<SingleOrderResponse> {

        try {
            val data = apiClient.getOrder(B_Token + token,orderNo)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun addNote(token: String, note: AddNoteParam): RetrofitResource<OrderNoteResponse> {
        try {
            val data = apiClient.addNote(AppConstance.B_Token+token,note)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if(data.errorBody()!=null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun changeOrderStatus(token: String,status:String,orderNo:String): RetrofitResource<SuccessResponse> {

        try {
            val data = apiClient.changeOrderStatus(B_Token + token,status,orderNo)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }


    override suspend fun changeOrderItemsStatus(token: String, orderNo:String, cancelOrder: RequestBody, product_pic: MultipartBody.Part?): RetrofitResource<SuccessResponse> {

        try {
            val data = apiClient.changeOrderItemsStatus(B_Token + token,orderNo,cancelOrder,product_pic)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }


    override suspend fun getPromos(
        token: String,
        pageNo: Int,
        pageSize: Int
    ): RetrofitResource<PromoResponse> {

        try {

            val data = apiClient.getPromos(B_Token + token, pageNo, pageSize, ProductParams())
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), "",
                )
            )
        }
    }


    override suspend fun getCategories(
        token: String
    ): RetrofitResource<CategorieResponse> {

        try {

            val data = apiClient.getCategories(B_Token + token)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), "",
                )
            )
        }
    }
    override suspend fun getSubCategories(
        token: String
    ): RetrofitResource<SubCategorieResponse> {

        try {

            val data = apiClient.getSubCategories(B_Token + token)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), "",
                )
            )
        }
    }


    override suspend fun resendOtp(forgetPasswordParams: ForgetPasswordParams): RetrofitResource<LoginResponses> {
        try {
            val forgetData = apiClient.resendOtp(forgetPasswordParams)
            val result = forgetData.body()
            if (forgetData.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (forgetData.errorBody() != null) {
                    return RetrofitResource.Error(handleError(forgetData.code(),forgetData.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getAddresses(token:String): RetrofitResource<ShippingAddressListResponse> {
        try {
            val data = apiClient.getAddressList(B_Token +token)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }
    override suspend fun addAddress(token:String,shippingAddressItem: ShippingAddressItem): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.addAddress(B_Token +token,shippingAddressItem)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }
    override suspend fun updateAddress(token:String,shippingAddressItem: ShippingAddressItem): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.updateAddress(B_Token +token,shippingAddressItem)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }
    override suspend fun deleteAddress(token:String,shippingDeleteParams: ShippingDeleteParams): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.deleteAddress(B_Token +token,shippingDeleteParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getAllCountries(): RetrofitResource<CountryResponse> {
        try {
            val data = apiClient.getAllCountries()
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getAllDisputes(
        token: String,
        pageNo: Int,
        pageSize: Int,
        disputeListParams: DisputeListParams
    ): RetrofitResource<DisputeListResponse> {
        try {
            val data = apiClient.getAllDispute(B_Token +token,pageNo, pageSize, disputeListParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getDispute(
        token: String,
        disputeId: String
    ): RetrofitResource<DisputeSingleResponse> {
        try {
            val data = apiClient.getDispute(B_Token +token,disputeId)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun sendDisputeMessage(
        token: String,
        sendMessageParams: SendMessageParams
    ): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.sendDisputeMessage(B_Token +token,sendMessageParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun raiseDipute(
        token: String,
        disputeJson: RequestBody,
        file: MultipartBody.Part?
    ): RetrofitResource<RaiseDisputeResponse> {
        try {
            val data = apiClient.raiseDipute(B_Token +token,disputeJson,file)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun addMoney(
        token: String,
        addMoneyParams: AddMoneyParams
    ): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.addMoney(B_Token +token,addMoneyParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    override suspend fun getWalletHistory(
        token: String,
        pageNo: Int,
        pageSize: Int,
        addMoneyParams: WallletMoneyParams
    ): RetrofitResource<WalletHistoryResponse> {
        try {
            val data = apiClient.getWalletHistory(B_Token +token,pageNo, pageSize, addMoneyParams)

            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }


    override suspend fun transferMoney(
        token: String,
        transferMoneyParams: TransferMoneyParams
    ): RetrofitResource<SuccessResponse> {
        try {
            val data = apiClient.transferMoney(B_Token +token,transferMoneyParams)
            val result = data.body()
            if (data.isSuccessful && result != null) {
                return RetrofitResource.Success(result)
            } else {
                if (data.errorBody() != null) {
                    return RetrofitResource.Error(handleError(data.code(),data.errorBody()!!.string()))
                }
                return RetrofitResource.Error(
                    FailResponse(
                        context.getString(R.string.please_try_after_sometime),
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            return RetrofitResource.Error(
                FailResponse(
                    e.message ?: context.getString(R.string.server_not_responding), ""
                )
            )
        }
    }

    fun handleError(code:Int,error:String):FailResponse{
        if (code == 403) {
            return FailResponse("", "", code)
        }
        val json = JSONObject(error)
        val status = json.getString("status")
        val message = json.getString("message")
        return FailResponse(message, status)
    }


}