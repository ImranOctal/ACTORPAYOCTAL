package com.octal.actorpay.repositories.retrofitrepository.models.misc

data class MiscChangePasswordParams(
    val currentPassword:String,
    val newPassword:String,
    val confirmPassword:String,
)


data class FAQResponseData (
     val id        : String,
     val question  : String,
     val answer    : String,
     val updatedAt : String
)

data class FAQResponse (
     val message    : String,
     val data       : List<FAQResponseData> = arrayListOf(),
     val status     : String,
     val httpStatus : String
)