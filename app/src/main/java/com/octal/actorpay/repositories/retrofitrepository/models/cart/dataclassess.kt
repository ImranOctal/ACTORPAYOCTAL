package com.octal.actorpay.repositories.retrofitrepository.models.cart


data class CartResponse(
    var message: String,
    var data: CartData,
    var status: String,
    var httpStatus: String

)

data class CartData(
    var totalQuantity: Int,
    var totalPrice: Double,
    var totalSgst: Double,
    var totalCgst: Double,
    var userId: String,
    var merchantId: String,
    var productName: String,
    var cartItemDTOList: MutableList<CartItemDTO> = mutableListOf(),
    var totalTaxableValue: Double

)


data class CartItemDTO(
    var createdAt: String,
    var updatedAt: String,
    var cartItemId: String,
    var productName: String,
    var productId: String,
    var productPrice: Double,
    var productQty: Int,
    var productSgst: Double,
    var productCgst: Double,
    var userDTO: UserDTO,
    var merchantId: String,
    var totalPrice: Double,
    var shippingCharge: Double,
    var taxPercentage: Double,
    var taxableValue: Double,
    var email: String,
    var image: String,
    var active: Boolean

)


data class UserDTO(
    var id: String,
    var invalidLoginAttempts: Int,
    var roles: ArrayList<String>,
    var kycDone: Boolean

)

data class CartParams(
    val productId:String,
    val productPrice:Double,
    val productQty:Int=1
)
data class CartUpdateParams(
    val cartItemId:String,
    val productQty:Int
)