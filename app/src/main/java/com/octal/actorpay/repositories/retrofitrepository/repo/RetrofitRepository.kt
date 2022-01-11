package com.octal.actorpay.repositories.retrofitrepository.repo

import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.SocialParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignupResponse
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileReesponse
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
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderParamas
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpay.repositories.retrofitrepository.models.promocodes.PromoResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpay.repositories.retrofitrepository.resource.RetrofitResource

/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */


interface RetrofitRepository {
    //Login Suspend Function
    suspend fun LoginNow(loginDetail: LoginParams): RetrofitResource<LoginResponses>

    suspend fun signUpNow(signupDetails:SignUpParams):RetrofitResource<SignupResponse>

    suspend fun socialLogin(signupDetails:SocialParams):RetrofitResource<LoginResponses>

    suspend fun ForgetPassword(forgetPasswordParams:ForgetPasswordParams):RetrofitResource<LoginResponses>

    suspend fun getProfile(id:String,token:String):RetrofitResource<ProfileReesponse>

    suspend fun saveProfile(email:String,extensionNumber:String,contactNumber:String,id:String,token: String):RetrofitResource<SuccessResponse>

    suspend fun sendOtp(token: String):RetrofitResource<SuccessResponse>

    suspend fun verifyOtp(otp:String,token: String):RetrofitResource<SuccessResponse>

    suspend fun changePassword(miscChangePasswordParams: MiscChangePasswordParams,token: String):RetrofitResource<SuccessResponse>

    suspend fun getContent(type:String):RetrofitResource<ContentResponse>

    suspend fun getFAQ():RetrofitResource<FAQResponse>

    suspend fun getProducts(token: String,pageNo:Int,pageSize:Int,productParams: ProductParams):RetrofitResource<ProductListResponse>

    suspend fun getCarts(token: String):RetrofitResource<CartResponse>

    suspend fun addCart(token: String,cartParams: CartParams):RetrofitResource<CartResponse>

    suspend fun deleteCart(token: String,cartId: String):RetrofitResource<CartResponse>

    suspend fun deleteAllCart(token: String):RetrofitResource<CartResponse>

    suspend fun updateCart(token: String,cartParams: CartUpdateParams):RetrofitResource<CartResponse>

    suspend fun placeOrder(token: String,placeOrderParamas: PlaceOrderParamas):RetrofitResource<PlaceOrderResponse>

    suspend fun getAllOrders(token: String,pageNo:Int,pageSize:Int,orderListParams: OrderListParams):RetrofitResource<OrderListResponse>

    suspend fun changeOrderStatus(token: String,status:String,orderNo:String):RetrofitResource<SuccessResponse>

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