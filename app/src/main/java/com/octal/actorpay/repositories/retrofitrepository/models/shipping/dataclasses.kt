package com.octal.actorpay.repositories.retrofitrepository.models.shipping

data class ShippingAddressListResponse(
    var message: String,
    var data: ShippingAddressListData,
    var status: String,
    var httpStatus: String
)

data class ShippingAddressListData(

    var totalPages: Int,
    var totalItems: Int,
    var items: MutableList<ShippingAddressItem>,
    var pageNumber: Int,
    var pageSize: Int

)


data class ShippingAddressItem(

    var addressLine1: String,
    var addressLine2: String?,
    var zipCode: String,
    var city: String,
    var state: String,
    var country: String,
    var latitude: String,
    var longitude: String,
    var name: String,
    var title: String,
    var area: String,
    var primaryContactNumber: String,
    var extensionNumber: String?,
    var secondaryContactNumber: String,
    var primary: Boolean,
    var isSelect:Boolean?=false,
    var userId: String,
    var id: String?=null,
)

data class ShippingDeleteParams(
    var ids:MutableList<String>
)

