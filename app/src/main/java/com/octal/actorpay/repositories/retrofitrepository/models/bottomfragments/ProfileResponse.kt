package com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments

import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.Data

data class ProfileReesponse(val message: String,
                            val status: String,
                            val `data`: ProfileResponseData)

data class ProfileResponseData (val id: String,
                       val firstName: String,
                       val lastName: String,
                       val gender: String,
                       val dateOfBirth: String? = null,
                       val email: String,
                       val extensionNumber: String,
                       val contactNumber: String,
                       val panNumber: String,
                       val aadharNumber: String,
                       val phoneVerified: Boolean,
                       val emailVerified: Boolean,
                       val active: Boolean,
                       val kycDone: Boolean)

data class ProfileParams(
    val email:String,
    val extensionNumber:String,
    val contactNumber:String,
    val id:String,
        )