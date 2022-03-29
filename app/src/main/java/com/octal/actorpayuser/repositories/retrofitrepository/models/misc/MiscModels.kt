package com.octal.actorpayuser.repositories.retrofitrepository.models.misc

data class MiscChangePasswordParams(
    val currentPassword:String,
    val newPassword:String,
    val confirmPassword:String,
)


data class FAQResponseData (
     val id        : String,
     val question  : String,
     val answer    : String,
     val updatedAt : String,
     var isExpand:Boolean=false
)

data class FAQResponse (
     val message    : String,
     val data       : List<FAQResponseData> = arrayListOf(),
     val status     : String,
     val httpStatus : String
)

data class CountryResponse (
     val message    : String,
     val data       : List<CountryItem> = arrayListOf(),
     val status     : String,
     val httpStatus : String
)

data class CountryItem(
    val country:String,
    val countryCode:String,
    val countryFlag:String?,
)