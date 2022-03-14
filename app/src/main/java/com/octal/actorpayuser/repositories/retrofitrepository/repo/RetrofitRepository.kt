package com.octal.actorpayuser.repositories.retrofitrepository.repo

import androidx.paging.PagingData
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.SocialParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.signup.SignUpParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.signup.SignupResponse
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
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.promocodes.PromoResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.*
import com.octal.actorpayuser.repositories.retrofitrepository.resource.RetrofitResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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

    suspend fun getProductsWithPaging(viewmodelscope:CoroutineScope,token: String,productParams: ProductParams): Flow<PagingData<ProductItem>>

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

    suspend fun deleteAddress(token:String,shippingDeleteParams: String):RetrofitResource<SuccessResponse>

    suspend fun getAllCountries():RetrofitResource<CountryResponse>

    suspend fun getAllDisputes(token: String,pageNo:Int,pageSize:Int,disputeListParams: DisputeListParams):RetrofitResource<DisputeListResponse>

    suspend fun getDispute(token: String,disputeId:String):RetrofitResource<DisputeSingleResponse>

    suspend fun sendDisputeMessage(token: String,sendMessageParams: SendMessageParams):RetrofitResource<SuccessResponse>

    suspend fun raiseDipute(token: String,  disputeJson: RequestBody, file: MultipartBody.Part?):RetrofitResource<RaiseDisputeResponse>

    suspend fun addMoney(token: String, addMoneyParams: AddMoneyParams ):RetrofitResource<AddMoneyResponse>

    suspend fun getWalletHistory(token: String,pageNo:Int,pageSize:Int,addMoneyParams: WallletMoneyParams):RetrofitResource<WalletHistoryResponse>

    suspend fun transferMoney(token: String, transferMoneyParams: TransferMoneyParams ):RetrofitResource<AddMoneyResponse>

    suspend fun getWalletBalance(token: String,id: String):RetrofitResource<WalletBalance>

    suspend fun userExists(token: String, user:String ):RetrofitResource<LoginResponses>

    suspend fun getAllRequestMoney(token: String,pageNo:Int,pageSize:Int, requestMoneyParams: GetAllRequestMoneyParams ):RetrofitResource<GetAllRequestMoneyResponse>

    suspend fun requestMoney(token: String,requestMoneyParams: RequestMoneyParams ):RetrofitResource<RequestMoneyResponse>

    suspend fun processRequest(token: String,isAccept:Boolean,requestId:String ):RetrofitResource<RequestProcessResponse>




}