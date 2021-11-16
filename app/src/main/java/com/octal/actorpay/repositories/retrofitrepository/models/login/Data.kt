package com.octal.actorpay.repositories.retrofitrepository.models.login

data class Data(
    val access_token: String,
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val refresh_token: String,
    val token_type: String
)