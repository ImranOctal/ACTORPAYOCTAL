package com.octal.actorpayuser.repositories.retrofitrepository.models.auth.signup

import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.DeviceInfoParams

data class SignUpParams(val firstName:String,
                        val lastName:String,
                        val email:String,
                        val extensionNumber:String,
                        val contactNumber:String,
                        val password:String,
                        val gender:String,
                        val dateOfBirth:String,
                        val panNumber:String,
                        val aadharNumber:String,
                        val deviceInfo: DeviceInfoParams
)