package com.octal.actorpayuser.repositories.retrofitrepository.models.products

data class ProductParams(
    val categoryName:String?=null,
    val name:String?=null
)

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

data class SingleProductResponse(
    val `data`: ProductItem,
    val httpStatus: String,
    val message: String,
    val status: String
)


data class ProductItem (
    val productId : String,
    val name : String,
    val description : String,
    val categoryId : String,
    val subCategoryId : String,
    val actualPrice : Double,
    val dealPrice : Double,
    val image : String,
    val merchantId : String,
    val merchantName : String,
    val stockCount : Int,
    val taxId : String,
    val stockStatus : String,
    val status : Boolean,
    val sgst : Double,
    val cgst : Double,
    val createdAt : String,
    val updatedAt : String
)
