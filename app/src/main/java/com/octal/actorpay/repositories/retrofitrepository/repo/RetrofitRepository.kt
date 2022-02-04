package com.octal.actorpay.repositories.retrofitrepository.repo

import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.SocialParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignupResponse
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
import com.octal.actorpay.repositories.retrofitrepository.models.order.*
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpay.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpay.repositories.retrofitrepository.models.promocodes.PromoResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface RetrofitRepository {
    //Login Suspend Function
    suspend fun loginNow(loginDetail: LoginParams): RetrofitResource<LoginResponses>

    suspend fun signUpNow(signupDetails:SignUpParams):RetrofitResource<SignupResponse>

    suspend fun socialLogin(signupDetails:SocialParams):RetrofitResource<LoginResponses>

    suspend fun forgetPassword(forgetPasswordParams:ForgetPasswordParams):RetrofitResource<LoginResponses>

    suspend fun getProfile(id:String,token:String):RetrofitResource<ProfileResponse>

    suspend fun saveProfile(email:String,extensionNumber:String,contactNumber:String,id:String,token: String):RetrofitResource<SuccessResponse>

    suspend fun sendOtp(token: String):RetrofitResource<SuccessResponse>

    suspend fun verifyOtp(otp:String,token: String):RetrofitResource<SuccessResponse>

    suspend fun changePassword(miscChangePasswordParams: MiscChangePasswordParams,token: String):RetrofitResource<SuccessResponse>

    suspend fun getContent(type:String):RetrofitResource<ContentResponse>

    suspend fun getFAQ():RetrofitResource<FAQResponse>

    suspend fun getProducts(token: String,pageNo:Int,pageSize:Int,productParams: ProductParams):RetrofitResource<ProductListResponse>

    suspend fun getProductById(token: String,id: String):RetrofitResource<SingleProductResponse>

    suspend fun getCarts(token: String):RetrofitResource<CartResponse>

    suspend fun addCart(token: String,cartParams: CartParams):RetrofitResource<CartResponse>

    suspend fun deleteCart(token: String,cartId: String):RetrofitResource<CartResponse>

    suspend fun deleteAllCart(token: String):RetrofitResource<CartResponse>

    suspend fun updateCart(token: String,cartParams: CartUpdateParams):RetrofitResource<CartResponse>

    suspend fun placeOrder(token: String,placeOrderParams: PlaceOrderParams):RetrofitResource<PlaceOrderResponse>

    suspend fun getAllOrders(token: String,pageNo:Int,pageSize:Int,orderListParams: OrderListParams):RetrofitResource<OrderListResponse>

    suspend fun getOrder(token: String,orderNo: String):RetrofitResource<SingleOrderResponse>

    suspend fun addNote(token: String, note: AddNoteParam): RetrofitResource<OrderNoteResponse>

    suspend fun changeOrderStatus(token: String,status:String,orderNo:String):RetrofitResource<SuccessResponse>

    suspend fun changeOrderItemsStatus(token: String, orderNo:String, cancelOrder: RequestBody, product_pic: MultipartBody.Part?):RetrofitResource<SuccessResponse>

    suspend fun getPromos(token: String,pageNo:Int,pageSize:Int):RetrofitResource<PromoResponse>

    suspend fun getCategories(token: String):RetrofitResource<CategorieResponse>

    suspend fun getSubCategories(token: String):RetrofitResource<SubCategorieResponse>

    suspend fun resendOtp(forgetPasswordParams:ForgetPasswordParams):RetrofitResource<LoginResponses>

    suspend fun getAddresses(token:String):RetrofitResource<ShippingAddressListResponse>

    suspend fun addAddress(token:String,shippingAddressItem: ShippingAddressItem):RetrofitResource<SuccessResponse>

    suspend fun updateAddress(token:String,shippingAddressItem: ShippingAddressItem):RetrofitResource<SuccessResponse>

    suspend fun deleteAddress(token:String,shippingDeleteParams: ShippingDeleteParams):RetrofitResource<SuccessResponse>

    suspend fun getAllCountries():RetrofitResource<CountryResponse>

}