package com.octal.actorpay.repositories.retrofitrepository.models.login

data class LoginResponses(
    val `data`: Data,
    val httpStatus: String,
    val message: String,
    val status: String
)