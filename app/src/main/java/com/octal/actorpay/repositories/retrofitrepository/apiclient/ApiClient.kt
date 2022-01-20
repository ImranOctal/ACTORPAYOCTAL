package com.octal.actorpay.repositories.retrofitrepository.apiclient

import com.octal.actorpay.repositories.AppConstance.AppConstance
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.ADDRESS_ADD
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.ADDRESS_DELETE
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.ADDRESS_LIST
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.ADDRESS_UPDATE
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.ADD_CART
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.CATEGORIE_LIST
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.CHANGE_PASSWORD
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.FORGETPASSWORD
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_ALL_CART
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_ALL_ORDERS
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_ALL_PRODUCTS
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_CONTENT
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_COUNTRIES
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_FAQ
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_PROFILE
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.GET_SINGLE_PRODUCT
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.LOGIN
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.ORDER_CANCEL
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.ORDER_STATUS
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.PLACE_ORDER
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.PROMO_LIST
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.RESEND_OTP
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.SEND_OTP
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.SIGNUP
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.SOCIAL_LOGIN
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.SUB_CATEGORIE_LIST
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.UPDATE_CART
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.UPDATE_PROFILE
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.VAR_ID
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.VERIFY_OTP
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
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListParams
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderParams
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpay.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpay.repositories.retrofitrepository.models.promocodes.PromoResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST(LOGIN)
    suspend fun loginNow(@Body loginDetail: LoginParams): Response<LoginResponses>

    @POST(SIGNUP)
    suspend fun signUp(@Body loginDetail: SignUpParams): Response<SignupResponse>


    @POST(SOCIAL_LOGIN)
    suspend fun socialLogin(@Body socialParams: SocialParams): Response<LoginResponses>


    @POST(FORGETPASSWORD)
    suspend fun forgetPassword(@Body forgetPasswordParams: ForgetPasswordParams): Response<LoginResponses>

    @POST(RESEND_OTP)
    suspend fun resendOtp(@Body forgetPasswordParams: ForgetPasswordParams): Response<LoginResponses>

    @GET("$GET_PROFILE{id}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ProfileResponse>

    @PUT(UPDATE_PROFILE)
    suspend fun saveProfile(
        @Header("Authorization") token: String,
        @Body profileParams: ProfileParams
    ): Response<SuccessResponse>


    @GET(SEND_OTP)
    suspend fun sendOtp(
        @Header("Authorization") token: String
    ): Response<SuccessResponse>


    @POST(VERIFY_OTP)
    suspend fun verifyOtp(
        @Header("Authorization") token: String,
        @Query("otp") type: String
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

    @POST(GET_ALL_PRODUCTS)
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Body productParams: ProductParams
    ): Response<ProductListResponse>


    @GET(GET_SINGLE_PRODUCT+ VAR_ID)
    suspend fun getProductById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<SingleProductResponse>

    @GET(GET_ALL_CART)
    suspend fun getCarts(
        @Header("Authorization") token: String,
    ): Response<CartResponse>

    @POST(ADD_CART)
    suspend fun addCart(
        @Header("Authorization") token: String,
        @Body cartParams: CartParams
    ): Response<CartResponse>


    @DELETE(AppConstance.DELETE_CART +"{id}")
    suspend fun deleteCart(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<CartResponse>

    @DELETE(AppConstance.DELETE_ALL_CART)
    suspend fun deleteAllCart(
        @Header("Authorization") token: String,
    ): Response<CartResponse>

    @PUT(UPDATE_CART)
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Body cartParams: CartUpdateParams
    ): Response<CartResponse>

    @POST(PLACE_ORDER)
    suspend fun placeOrder(
        @Header("Authorization") token: String,
        @Body placeOrderParams: PlaceOrderParams
    ): Response<PlaceOrderResponse>

    @POST(GET_ALL_ORDERS)
    suspend fun getAllOrders(
        @Header("Authorization") token: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Body orderListParams: OrderListParams
    ): Response<OrderListResponse>


    @PUT("""$ORDER_STATUS{status}""")
    suspend fun changeOrderStatus(
        @Header("Authorization") token: String,
        @Path("status") status: String,
        @Query("orderNo") orderNo: String,
    ): Response<SuccessResponse>

    @Multipart
    @POST("$ORDER_CANCEL{order}")
    suspend fun changeOrderItemsStatus(
        @Header("Authorization") token: String,
        @Path("order") order: String,
        @Part(AppConstance.CANCEL_ORDER) product: RequestBody,
        @Part file: MultipartBody.Part?,
    ): Response<SuccessResponse>



    @POST(PROMO_LIST)
    suspend fun getPromos(
        @Header("Authorization") token: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Body productParams: ProductParams
    ): Response<PromoResponse>

    @GET(CATEGORIE_LIST)
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): Response<CategorieResponse>


    @GET(SUB_CATEGORIE_LIST)
    suspend fun getSubCategories(
        @Header("Authorization") token: String,
        @Query("pageNo") pageNo: Int=0,
        @Query("pageSize") pageSize: Int=50,
    ): Response<SubCategorieResponse>


    @GET(ADDRESS_LIST)
    suspend fun getAddressList(
        @Header("Authorization") token: String,
        @Query("pageNo") pageNo: Int=0,
        @Query("pageSize") pageSize: Int=50,
        @Query("sortBy") sortBy: String="createdAt",
        @Query("asc") asc: Boolean=false,
    ): Response<ShippingAddressListResponse>


    @POST(ADDRESS_ADD)
    suspend fun addAddress(
        @Header("Authorization") token: String,
        @Body shippingAddressItem: ShippingAddressItem
    ): Response<SuccessResponse>

    @PUT(ADDRESS_UPDATE)
    suspend fun updateAddress(
        @Header("Authorization") token: String,
        @Body shippingAddressItem: ShippingAddressItem
    ): Response<SuccessResponse>

    @HTTP(method = "DELETE", path = ADDRESS_DELETE, hasBody = true)
    suspend fun deleteAddress(
        @Header("Authorization") token: String,
        @Body shippingDeleteParams: ShippingDeleteParams
    ): Response<SuccessResponse>


    @GET(GET_COUNTRIES)
    suspend fun getAllCountries(
    ): Response<CountryResponse>

}