package com.octal.actorpay.repositories.AppConstance

class AppConstance {
    companion object{
        const val BASE_URL: String = "http://192.168.1.171:8765/"
        const val SUB_DOMAIN: String = "api/"
        const val SUB_DOMAIN2: String = "user-service/"
        const val SUB_DOMAIN3: String = "users/"
        const val SUB_DOMAIN_FORGET: String = "forget/"
        const val SUB_DOMAIN_CMS: String = "cms-service/"
        const val BY:String="by/"
        const val ID:String="id/"
        const val B_Token:String="Bearer "
        const val UPDATE:String="update/"
        const val PRODUCT:String="products"
        const val CART:String="cartitems/"
        const val LOGIN: String = SUB_DOMAIN+SUB_DOMAIN2+SUB_DOMAIN3+"login"
        const val SIGNUP: String = SUB_DOMAIN+SUB_DOMAIN2+SUB_DOMAIN3+"signup"
        const val SOCIAL_LOGIN: String = SUB_DOMAIN+SUB_DOMAIN2+SUB_DOMAIN3+"social/signup"
        const val FORGETPASSWORD: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+SUB_DOMAIN_FORGET+"password"
        const val GET_PROFILE: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ BY+ ID
        const val UPDATE_PROFILE: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ UPDATE
        const val CHANGE_PASSWORD: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ "change/password"
        const val GET_CONTENT: String = SUB_DOMAIN+ SUB_DOMAIN_CMS+  "get/static/data/by/cms"
        const val GET_FAQ: String = SUB_DOMAIN+ SUB_DOMAIN_CMS+  "faq/all"



        const val GET_ALL_PRODUCTS: String = SUB_DOMAIN+ SUB_DOMAIN2+ PRODUCT
        const val GET_ALL_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"view"
        const val ADD_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"add"
        const val DELETE_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"remove/"
        const val UPDATE_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"update"
    }
}

enum class Clicks{
    AddCart,
    Root,
    Minus,
    Plus,
    BuyNow,
    Delete
}