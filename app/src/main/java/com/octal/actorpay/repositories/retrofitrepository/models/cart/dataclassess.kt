package com.octal.actorpay.repositories.retrofitrepository.models.cart


data class CartResponse(
    var message: String? = null,
    var data: CartData,
    var status: String? = null,
    var httpStatus: String? = null

)

data class CartData(
    var totalQuantity: Int,
    var totalPrice: Double,
    var totalSgst: Double,
    var totalCgst: Double,
    var userId: String? = null,
    var merchantId: String? = null,
    var productName: String? = null,
    var cartItemDTOList: MutableList<CartItemDTO> = mutableListOf(),
    var totalTaxableValue: Double

)


data class CartItemDTO(
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var cartItemId: String,
    var productName: String? = null,
    var productId: String? = null,
    var productPrice: Int? = null,
    var productQty: Int,
    var productSgst: Int? = null,
    var productCgst: Int? = null,
    var userDTO: UserDTO? = UserDTO(),
    var merchantId: String? = null,
    var totalPrice: Int? = null,
    var shippingCharge: Int? = null,
    var taxPercentage: Int? = null,
    var taxableValue: Int? = null,
    var email: String? = null,
    var active: Boolean? = null

)


data class UserDTO(
    var id: String? = null,
    var invalidLoginAttempts: Int? = null,
    var roles: ArrayList<String> = arrayListOf(),
    var kycDone: Boolean? = null

)

data class CartParams(
    val productId:String,
    val productQty:Int=1
)
data class CartUpdateParams(
    val cartItemId:String,
    val productQty:Int
)