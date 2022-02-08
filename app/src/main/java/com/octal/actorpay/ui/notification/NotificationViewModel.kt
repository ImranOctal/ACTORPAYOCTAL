package com.octal.actorpay.ui.notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository

class NotificationViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

}