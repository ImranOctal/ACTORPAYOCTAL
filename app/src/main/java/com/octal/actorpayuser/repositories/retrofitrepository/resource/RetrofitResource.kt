package com.octal.actorpayuser.repositories.retrofitrepository.resource

import com.octal.actorpayuser.repositories.retrofitrepository.models.FailResponse


sealed class RetrofitResource<T>(val data: T?, val message: FailResponse?) {
    class Success<T>(data: T?) : RetrofitResource<T>(data, null)
    class Error<T>(message: FailResponse?) : RetrofitResource<T>(null, message)
}