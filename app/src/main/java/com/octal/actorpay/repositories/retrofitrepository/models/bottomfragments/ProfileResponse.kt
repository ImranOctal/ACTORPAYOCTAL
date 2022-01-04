package com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments

import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.Data

data class ProfileReesponse(val message: String,
                            val status: String,
                            val `data`: ProfileResponseData)

data class ProfileResponseData (val id: String,
                       val firstName: String,
                       val lastName: String,
                       val gender: String,
                       val dateOfBirth: Any? = null,
                       val email: String,
                       val profilePicture: Any? = null,
                       val extensionNumber: String,
                       val contactNumber: String,
                       val userType: Any? = null,
                       val createdAt: String,
                       val updatedAt: String,
                       val phoneVerified: Boolean,
                       val emailVerified: Boolean,
                       val roles: List<Any?>,
                       val active: Boolean,
                       val kycDone: Boolean)

data class ProfileParams(
    val email:String,
    val extensionNumber:String,
    val contactNumber:String,
    val id:String,
        )