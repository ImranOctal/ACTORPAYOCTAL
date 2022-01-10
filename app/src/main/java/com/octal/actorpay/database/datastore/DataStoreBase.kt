package com.octal.actorpay.database.datastore
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */
import kotlinx.coroutines.flow.Flow

interface DataStoreBase {

    fun giveRepository(): String

    suspend fun logOut(): Unit


    suspend fun setPhoneNumber(mobileNumber: String)

    suspend fun setCountryCode(countryCode : String)

    suspend fun setFirstName(name:String)

    suspend fun setLastName(name:String)

    suspend fun setUserId(userId:String)

    suspend fun setEmail(email:String)

    suspend fun setIsLoggedIn(value:Boolean)

    suspend fun setIsIntro(value:Boolean)

    suspend fun setAccessToken(value:String)

    suspend fun setRefreshToken(value:String)

    fun getBoolean(): Flow<Boolean>

    fun isLoggedIn(): Flow<Boolean>

    fun isIntro(): Flow<Boolean>

    fun getString(): Flow<String>

    fun getUserId(): Flow<String>

    fun getEmail(): Flow<String>

    fun getMobileNumber(): Flow<String>

    fun getFirstName():Flow<String>

    fun getLastName():Flow<String>

    fun getCountryCode(): Flow<String>

    fun getAccessToken(): Flow<String>

    fun getRefreshToken(): Flow<String>

}