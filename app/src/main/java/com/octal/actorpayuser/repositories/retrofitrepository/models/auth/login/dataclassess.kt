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


data class UserDetailsResponse(
    val `data`: UserDetailsData,
    val httpStatus: String,
    val message: String,
    val status: String
)

data class UserDetailsData(
    val customerDetails: CustomerDetailsData?,
    val merchantDetails: MerchantDetailsData?
)

data class CustomerDetailsData(
    val userId:String,
    val firstName:String,
    val lastName:String,
    val email:String,
    val contactNumber:String,
    val extension:String,
    val userType:String,
)

data class MerchantDetailsData(
    val userId:String,
    val merchantId:String,
    val businessName:String,
    val email:String,
    val contactNumber:String,
    val extension:String,
    val userType:String,
)