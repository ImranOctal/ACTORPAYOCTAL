package com.octal.actorpayuser.repositories.retrofitrepository.apiclient

import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ADDRESS_ADD
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ADDRESS_DELETE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ADDRESS_LIST
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ADDRESS_UPDATE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ADD_CART
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ADD_MONEY
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.Add_Note
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.CATEGORIE_LIST
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.CHANGE_PASSWORD
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.FORGETPASSWORD
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_ALL_CART
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_ALL_DISPUTES
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_ALL_ORDERS
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_ALL_PRODUCTS
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_CONTENT
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_COUNTRIES
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_DISPUTE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_FAQ
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_PROFILE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_SINGLE_PRODUCT
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.GET_WALLET_BALANCE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.LOGIN
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ORDER_CANCEL
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.ORDER_STATUS
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.PLACE_ORDER
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.PROMO_LIST
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.RAISE_DISPUTE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.RESEND_OTP
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.SEND_DISPUTE_MESSAGE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.SEND_OTP
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.SIGNUP
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.SOCIAL_LOGIN
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.SUB_CATEGORIE_LIST
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.TRANSFER_MONEY
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.UPDATE_CART
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.UPDATE_PROFILE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.USER_EXISTS
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.VAR_ID
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.VERIFY_OTP
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.WALLET_HISTORY
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
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.*
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.CountryResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.FAQResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.MiscChangePasswordParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.*
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.promocodes.PromoResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.*
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

  @GET("$PLACE_ORDER{order}")
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Path("order") order: String,
    ): Response<SingleOrderResponse>


    @POST(Add_Note)
    suspend fun addNote(
        @Header("Authorization") token: String,
        @Body  addNoteParam: AddNoteParam,
    ): Response<OrderNoteResponse>


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


    @POST(GET_ALL_DISPUTES)
    suspend fun getAllDispute(
        @Header("Authorization") token: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Body disputeListParams: DisputeListParams,
        @Query("asc") asc: Boolean=false
    ): Response<DisputeListResponse>

    @GET("$GET_DISPUTE{id}")
    suspend fun getDispute(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<DisputeSingleResponse>

    @POST(SEND_DISPUTE_MESSAGE)
    suspend fun sendDisputeMessage(
        @Header("Authorization") token: String,
        @Body sendMessageParams: SendMessageParams,
    ): Response<SuccessResponse>

    @Multipart
    @POST(RAISE_DISPUTE)
    suspend fun raiseDipute(
        @Header("Authorization") token: String,
        @Part(AppConstance.DISPUTE) product: RequestBody,
        @Part file: MultipartBody.Part?,
    ): Response<RaiseDisputeResponse>

    @POST(ADD_MONEY)
    suspend fun addMoney(
        @Header("Authorization") token: String,
        @Body addMoneyParams: AddMoneyParams,
    ): Response<SuccessResponse>

    @POST(WALLET_HISTORY)
    suspend fun getWalletHistory(
        @Header("Authorization") token: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Body addMoneyParams: WallletMoneyParams
    ): Response<WalletHistoryResponse>

    @POST(TRANSFER_MONEY)
    suspend fun transferMoney(
        @Header("Authorization") token: String,
        @Body transferMoneyParams: TransferMoneyParams,
    ): Response<SuccessResponse>


    @GET(GET_WALLET_BALANCE+ VAR_ID+"/balance")
    suspend fun getWalletBalance(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<WalletBalance>


    @GET("$USER_EXISTS{user}/get")
    suspend fun userExists(
        @Header("Authorization") token: String,
        @Path("user") user: String
    ): Response<LoginResponses>

}