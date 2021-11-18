package com.octal.actorpay.repositories.retrofitrepository.models.auth.signup

import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.Data

data class SignupResponse(
    val `data`: Data,
    val httpStatus: String,
    val message: String,
    val status: String
)