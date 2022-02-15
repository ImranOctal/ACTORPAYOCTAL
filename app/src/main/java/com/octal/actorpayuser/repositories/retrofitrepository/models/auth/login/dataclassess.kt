package com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login

data class LoginParams(val email: String,val password: String,val deviceInfo:DeviceInfoParams)

data class SocialParams(val firstName: String,val lastName: String,val login_type:String,val email: String,val googleId: String,val imageUrl: String,val deviceInfo:DeviceInfoParams)

data class ForgetPasswordParams(val emailId: String)

data class DeviceInfoParams(
    val deviceType:String,
    val appVersion:String,
    val deviceToken:String,
    val deviceData:String,
)



