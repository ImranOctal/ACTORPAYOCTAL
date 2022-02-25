package com.octal.actorpayuser.base

import com.octal.actorpayuser.repositories.retrofitrepository.models.FailResponse

sealed class ResponseSealed {
    class Success(val response: Any) : ResponseSealed()
    class ErrorOnResponse(val message: FailResponse?) : ResponseSealed()
    class loading(val isLoading: Boolean) : ResponseSealed()
    object Empty : ResponseSealed()
    }