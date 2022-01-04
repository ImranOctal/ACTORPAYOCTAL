package com.octal.actorpay.repositories.retrofitrepository.models.promocodes

data class PromoResponse(

    var message: String,
    var data: PromoData,
    var status: String,
    var httpStatus: String
)


data class PromoData(

    var totalPages: Int,
    var totalItems: Int,
    var items: MutableList<PromoItem> = mutableListOf(),
    var pageNumber: Int,
    var pageSize: Int

)


data class PromoItem(

    var offerId: String,
    var offerTitle: String,
    var offerDescription: String,
    var offerInPercentage: Int,
    var promoCode: String,
    var categoryId: String,
    var offerType: String,
    var createdAt: String,
    var updatedAt: String,
    var numberOfUsage: Int,
    var ordersPerDay: Int,
    var visibilityLevel: String,
    var active: Boolean

)