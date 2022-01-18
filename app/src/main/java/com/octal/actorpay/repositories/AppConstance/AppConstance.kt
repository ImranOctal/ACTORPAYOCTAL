package com.octal.actorpay.repositories.AppConstance

class AppConstance {
    companion object{
        const val BASE_URL: String = "http://192.168.1.171:8765/"
        const val SUB_DOMAIN: String = "api/"
        const val SUB_DOMAIN2: String = "user-service/"
        const val SUB_DOMAIN3: String = "users/"
        const val SUB_DOMAIN_FORGET: String = "forget/"
        const val SUB_DOMAIN_CMS: String = "cms-service/"
        const val SUB_DOMAIN_GLOBAL: String = "global-service/"
        const val BY:String="by/"
        const val ID:String="id/"
        const val VAR_ID:String="{id}"
        const val B_Token:String="Bearer "
        const val UPDATE:String="update/"
        const val PRODUCT:String="products/"
        const val CART:String="cartitems/"
        const val ORDER:String="orders/"
        const val OFFER:String="offers/"
        const val CANCEL_ORDER:String="cancelOrder"
        const val LOGIN: String = SUB_DOMAIN+SUB_DOMAIN2+SUB_DOMAIN3+"login"
        const val SIGNUP: String = SUB_DOMAIN+SUB_DOMAIN2+SUB_DOMAIN3+"signup"
        const val SOCIAL_LOGIN: String = SUB_DOMAIN+SUB_DOMAIN2+SUB_DOMAIN3+"social/signup"
        const val FORGETPASSWORD: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+SUB_DOMAIN_FORGET+"password"
        const val RESEND_OTP: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+"resend/activation/link/request"
        const val GET_PROFILE: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ BY+ ID
        const val UPDATE_PROFILE: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ UPDATE

        const val SEND_OTP: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ "phone/otp/request"
        const val VERIFY_OTP: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ "phone/verify"

        const val CHANGE_PASSWORD: String = SUB_DOMAIN+SUB_DOMAIN2+ SUB_DOMAIN3+ "change/password"
        const val GET_CONTENT: String = SUB_DOMAIN+ SUB_DOMAIN_CMS+  "get/static/data/by/cms"
        const val GET_FAQ: String = SUB_DOMAIN+ SUB_DOMAIN_CMS+  "faq/all"



        const val GET_ALL_PRODUCTS: String = SUB_DOMAIN+ SUB_DOMAIN2+ PRODUCT+"list/paged"
        const val GET_SINGLE_PRODUCT: String = SUB_DOMAIN+ SUB_DOMAIN2+ PRODUCT
        const val GET_ALL_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"view"
        const val ADD_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"add"
        const val DELETE_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"remove/"
        const val DELETE_ALL_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"clear"
        const val UPDATE_CART: String = SUB_DOMAIN+ SUB_DOMAIN2+ CART+"update"


        const val PLACE_ORDER: String = SUB_DOMAIN+ SUB_DOMAIN2+ ORDER
        const val GET_ALL_ORDERS: String = SUB_DOMAIN+ SUB_DOMAIN2+ ORDER+"list/paged"
        const val ORDER_STATUS: String = SUB_DOMAIN+ SUB_DOMAIN2+ ORDER+"status/"

        const val ORDER_CANCEL: String = SUB_DOMAIN+ SUB_DOMAIN2+ ORDER+"cancel/"


        const val PROMO_LIST: String = SUB_DOMAIN+ SUB_DOMAIN2+ OFFER+"available"
        const val CATEGORIE_LIST: String = SUB_DOMAIN+ SUB_DOMAIN2+"get/all/active/categories"
        const val SUB_CATEGORIE_LIST: String = SUB_DOMAIN+ SUB_DOMAIN2+"get/all/subcategories/paged"

        const val ADDRESS_LIST: String = SUB_DOMAIN+ SUB_DOMAIN2+"get/all/user/shipping/address"
        const val ADDRESS_ADD: String = SUB_DOMAIN+ SUB_DOMAIN2+"add/new/shipping/address"
        const val ADDRESS_UPDATE: String = SUB_DOMAIN+ SUB_DOMAIN2+"update/shipping/address"
        const val ADDRESS_DELETE: String = SUB_DOMAIN+ SUB_DOMAIN2+"delete/saved/shipping/address/ids"

        const val GET_COUNTRIES: String = SUB_DOMAIN+ SUB_DOMAIN_GLOBAL+"v1/country/get/all"

        const val STATUS_SUCCESS:String="SUCCESS"
        const val STATUS_READY:String="READY"
        const val STATUS_CANCELLED:String="CANCELLED"
        const val STATUS_PARTIALLY_CANCELLED:String="PARTIALLY_CANCELLED"
        const val STATUS_DISPATCHED:String="DISPATCHED"
        const val STATUS_RETURNING:String="RETURNING"
        const val STATUS_PARTIALLY_RETURNING:String="PARTIALLY_RETURNING"
        const val STATUS_RETURNED :String="RETURNED "
        const val STATUS_PARTIALLY_RETURNED:String="PARTIALLY_RETURNED"
        const val STATUS_DELIVERED:String="DELIVERED"
        const val STATUS_PENDING:String="PENDING"
        const val STATUS_FAILED:String="FAILED"
    }
}

enum class Clicks{
    AddCart,
    Root,
    Minus,
    Plus,
    BuyNow,
    Delete,
    Cancel,
    Details
}