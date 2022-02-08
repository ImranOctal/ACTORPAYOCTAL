package com.octal.actorpayuser.ui.mobileanddth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository

class MobileAndDthViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository)  : AndroidViewModel(
    Application()
) {

}