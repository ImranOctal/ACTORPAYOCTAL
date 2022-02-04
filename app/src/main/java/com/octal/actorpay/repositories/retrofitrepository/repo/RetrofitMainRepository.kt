package com.octal.actorpay.repositories.retrofitrepository.repo
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */

import android.content.Context
import com.octal.actorpay.R
import com.octal.actorpay.repositories.AppConstance.AppConstance
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
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartParams
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartResponse
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartUpdateParams
import com.octal.actorpay.repositories.retrofitrepository.models.categories.CategorieResponse
import com.octal.actorpay.repositories.retrofitrepository.models.categories.SubCategorieResponse
import com.octal.actorpay.repositories.retrofitrepository.models.content.ContentResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.CountryResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.FAQResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpay.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpay.repositories.retrofitrepository.models.promocodes.PromoResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import com.octal.actorpay.repositories.retrofitrepository.apiclient.ApiClient
import com.octal.actorpay.repositories.retrofitrepository.models.order.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

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