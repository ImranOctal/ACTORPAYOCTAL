package com.octal.actorpay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.octal.actorpay.database.prefrence.SharedPre
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository

class ActorPayViewModel(val dispatcherProvider: CoroutineContextProvider, val methodRepo: MethodsRepo, val apiRepo: RetrofitRepository,val shared:SharedPre)  : AndroidViewModel(
    Application()
) {

}