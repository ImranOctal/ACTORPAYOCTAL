package com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login

data class LoginResponses(
    val `data`: Data,
    val httpStatus: String,
    val message: String,
    val status: String
)