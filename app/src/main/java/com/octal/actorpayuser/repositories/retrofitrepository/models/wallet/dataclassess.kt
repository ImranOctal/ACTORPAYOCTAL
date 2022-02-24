package com.octal.actorpayuser.repositories.retrofitrepository.models.wallet

import java.io.Serializable


data class AddMoneyParams(
    var transferAmount:String?
)

data class TransferMoneyParams(
    var beneficiaryEmailId:String,
    var transferAmount:String,
    var transactionRemark:String
)

data class WallletMoneyParams(
    var walletTransactionId:String,
    var transactionAmountTo:String,
    var transactionAmountFrom:String,
    var transactionRemark:String,
    var transactionType:String,
)


data class WalletHistoryResponse(

    var message: String,
    var data: WalletListData,
    var status: String,
    var httpStatus: String

)


data class WalletListData(
    var totalPages: Int,
    var totalItems: Int,
    var items: MutableList<WalletData>,
    var pageNumber: Int,
    val pageSize: Int
)

data class WalletData(

    var createdAt: String,
    var updatedAt: String,
    var walletTransactionId: String,
    var transactionAmount: Double,
    var transactionTypes: String,
    var userType: String,
    var userId: String,
    var walletId: String,
    var adminCommission: Double,
    var transferAmount: Double,
    var purchaseType: String,
    var transactionRemark: String,
    var percentage: Double
    ): Serializable
