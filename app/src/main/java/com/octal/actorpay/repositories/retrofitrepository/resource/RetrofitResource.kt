package com.octal.actorpay.repositories.retrofitrepository.resource

import com.octal.actorpay.repositories.retrofitrepository.models.FailResponse


sealed class RetrofitResource<T>(val data: T?, val message: FailResponse?) {
    class Success<T>(data: T?) : RetrofitResource<T>(data, null)
    class Error<T>(message: FailResponse?) : RetrofitResource<T>(null, message)
}