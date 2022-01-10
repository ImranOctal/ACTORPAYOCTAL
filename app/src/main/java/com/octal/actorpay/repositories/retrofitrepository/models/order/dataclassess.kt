package com.octal.actorpay.repositories.retrofitrepository.models.order

import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductItem
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem


data class PlaceOrderResponse(

    var message: String,
    var data: OrderData,
    var status: String,
    var httpStatus: String

)

data class PlaceOrderParamas(
    var addressLine1:String,
    var addressLine2:String,
    var zipCode:String,
    var city:String,
    var state:String,
    var country:String,
    var primaryContactNumber:String,
    var secondaryContactNumber:String
)

data class OrderListResponse(
    var message: String,
    var data: OrderListData,
    var status: String,
    var httpStatus: String
)

data class OrderListParams(
    var totalPrice:Double?=null,
    var merchantId:String?=null,
    var startDate:String?=null,
    var endDate:String?=null,
    var orderNo:String?=null,
    var orderStatus:String?=null,
)

data class OrderListData (
    var totalPages : Int,
    var totalItems : Int,
    var items : MutableList<OrderData>,
    var pageNumber : Int,
    val pageSize : Int
)


data class OrderData(

    var orderId: String,
    var orderNo: String,
    var totalQuantity: Int,
    var totalPrice: Double,
    var totalSgst: Double,
    var totalCgst: Double,
    var customer: Customer,
    var merchantId: String,
    var merchantName: String,
    var orderStatus: String,
    var orderItemDtos: ArrayList<OrderItemDtos>,
    var createdAt: String,
    var totalTaxableValue: Double,
    val shippingAddressDTO:ShippingAddressItem?

)


data class Customer(

    var id: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var contactNumber: String,
    var roles: ArrayList<String>,
    var kycDone: Boolean

)

data class OrderItemDtos(

    var createdAt: String,
    var deleted: Boolean,
    var productId: String,
    var productName: String,
    var productPrice: Double,
    var productQty: Int,
    var productSgst: Double,
    var productCgst: Double,
    var totalPrice: Double,
    var shippingCharge: Double,
    var taxPercentage: Double,
    var taxableValue: Double,
    var categoryId: String,
    var subcategoryId: String,
    var image: String,
    var active: Boolean

)