package com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments

data class ProfileResponse (val id: String,
                       val firstName: String,
                       val lastName: String,
                       val gender: String,
                       val dateOfBirth: Any? = null,
                       val email: String,
                       val profilePicture: Any? = null,
                       val extensionNumber: String,
                       val contactNumber: String,
                       val userType: Any? = null,
                       val lastLoginDate: Any? = null,
                       val invalidLoginAttempts: Long,
                       val password: String,
                       val createdAt: String,
                       val updatedAt: String,
                       val roles: List<Any?>,
                       val active: Boolean,
                       val kycDone: Boolean)

data class ProfileParams(
    val email:String,
    val extensionNumber:String,
    val contactNumber:String,
    val id:String,
        )