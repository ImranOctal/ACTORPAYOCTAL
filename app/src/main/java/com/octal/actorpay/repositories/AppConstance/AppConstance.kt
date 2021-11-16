package com.octal.actorpay.repositories.AppConstance

class AppConstance {
    companion object{
        const val BASE_URL: String = "http://192.168.1.171:8765/"
        const val SUB_DOMAIN: String = "api/"
        const val SUB_DOMAIN2: String = "user-service/"
        const val SUB_DOMAIN3: String = "users/"
        const val LOGIN: String = SUB_DOMAIN+SUB_DOMAIN2+SUB_DOMAIN3+"login"
    }
}