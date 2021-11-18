package com.octal.actorpay.repositories.retrofitrepository.models.misc

data class MiscChangePasswordParams(
    val currentPassword:String,
    val newPassword:String,
    val confirmPassword:String,
)