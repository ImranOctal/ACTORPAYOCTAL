package com.octal.actorpayuser.repositories.retrofitrepository.models

import com.octal.actorpayuser.repositories.retrofitrepository.models.bottomfragments.ProfileResponseData
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartData


data class FailResponse(
    val message: String,
    val status: String,
    val code:Int=0
)
data class SuccessResponse(
    val message: String,
    val status: String,
)

data class GlobalResponse(
    var message: String,
    var data: GlobalResponseData,
    var status: String,
    var httpStatus: String
)

data class GlobalResponseData(
    var wallet_balance: Double,
    var notification_counter: Int,
    val userDTO: ProfileResponseData,
    val cartDTO: CartData
)