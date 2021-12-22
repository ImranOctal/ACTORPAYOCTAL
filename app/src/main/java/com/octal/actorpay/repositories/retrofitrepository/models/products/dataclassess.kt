package com.octal.actorpay.repositories.retrofitrepository.models.products



data class ProductListResponse(
    val `data`: ProductData,
    val httpStatus: String,
    val message: String,
    val status: String
)


data class ProductData (
    var totalPages : Int,
    var totalItems : Int,
    var items : MutableList<ProductItem>,
    var pageNumber : Int,
    val pageSize : Int
)


data class ProductItem (
    val productId : String,
    val name : String,
    val description : String,
    val categoryId : String,
    val subCategoryId : String,
    val actualPrice : Int,
    val dealPrice : Int,
    val image : String,
    val merchantId : String,
    val stockCount : Int,
    val taxId : String,
    val stockStatus : String,
    val status : Boolean,
    val createdAt : String,
    val updatedAt : String
)
