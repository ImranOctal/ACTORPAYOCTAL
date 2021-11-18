package com.octal.actorpay.repositories.retrofitrepository.models.auth.login

data class LoginParams(val email: String,val password: String)

data class SocialParams(val firstName: String,val lastName: String,val email: String,val googleId: String,val imageUrl: String)

data class ForgetPasswordParams(val emailId: String)

