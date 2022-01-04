package com.octal.actorpay.repositories.retrofitrepository.models.categories

import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartData

data class CategorieResponse(
    var message: String,
    var data: MutableList<CategorieItem>,
    var status: String,
    var httpStatus: String
)

data class CategorieItem(
    var id: String,
    var name: String,
    var description: String,
    var image: String,
    var isSelected:Boolean=false
)

data class SubCategorieResponse(
    var message: String,
    var data: MutableList<CategorieItem>,
    var status: String,
    var httpStatus: String
)

data class SubCategorieItem(
    var id: String,
    var name: String,
    var description: String,
    var image: String,
    var isSelected:Boolean=false,
    var categoryId:String,
    var categoryName:String
)
