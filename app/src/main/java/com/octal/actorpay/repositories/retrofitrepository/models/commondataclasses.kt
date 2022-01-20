package com.octal.actorpay.repositories.retrofitrepository.models


data class FailResponse(
    val message: String,
    val status: String,
    val code:Int=0
)
data class SuccessResponse(
    val message: String,
    val status: String,
)

