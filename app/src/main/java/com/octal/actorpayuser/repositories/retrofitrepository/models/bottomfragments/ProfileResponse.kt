package com.octal.actorpayuser.repositories.retrofitrepository.models.bottomfragments


data class ProfileResponse(val message: String,
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