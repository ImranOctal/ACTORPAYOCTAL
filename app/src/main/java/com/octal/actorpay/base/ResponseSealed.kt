package com.octal.actorpay.base

import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel

sealed class ResponseSealed {
    class Success(val response: Any) : ResponseSealed()
    class ErrorOnResponse(val message: FailResponse?) : ResponseSealed()
    class loading(val isLoading: Boolean?) : ResponseSealed()
    object Empty : ResponseSealed()
    }