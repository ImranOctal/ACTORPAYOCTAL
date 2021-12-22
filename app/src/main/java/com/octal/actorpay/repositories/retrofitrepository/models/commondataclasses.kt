package com.octal.actorpay.repositories.retrofitrepository.models

import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.Data

data class FailResponse(
    val message: String,
    val status: String,
    val code:Int=0
)
data class SuccessResponse(
    val message: String,
    val status: String,
    val `data`: Data,
)

