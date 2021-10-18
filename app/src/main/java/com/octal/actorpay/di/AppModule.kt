package com.octal.actorpay.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.octal.actorpay.database.datastore.*
import com.octal.actorpay.database.prefrence.SharedPre
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.RetrofitMainRepository
import com.octal.actorpay.repositories.retrofitrepository.RetrofitRepository
import com.octal.actorpay.retrofitrepository.apiclient.ApiClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.android.ext.koin.androidContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val appKoinModule = module {


    single { CoroutineContextProvider() }

    single { DataStoreCoroutinesHandler }

    single { SharedPre(androidContext()) }

    single<DataStoreBase>{
        DataStoreCustom(androidContext())
    }

    single{
        MethodsRepo(context = androidContext(),dataStore = get())
    }

    single<OkHttpClient>{
        OkHttpClient.Builder()
            /* .addInterceptor(okhttp3.Interceptor {chain ->
               val request:Request=chain.request().newBuilder().addHeader("Authorization", "Bearer " + sharedPre.jwtToken).build()
                 chain.proceed(request)
             })*/
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
    single<okhttp3.Interceptor>{
        okhttp3.Interceptor {chain ->
            val request: Request =chain.request().newBuilder().addHeader("Authorization", "Bearer " + "token").build()
            chain.proceed(request)
        }
    }
    single<ApiClient>{
        Retrofit.Builder().baseUrl("")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiClient::class.java)
    }
    single <RetrofitRepository>{
        RetrofitMainRepository(androidContext(),apiClient =get())
    }

    viewModel {
        ActorPayViewModel(dispatcherProvider = get(),methodRepo=get(),apiRepo = get())
    }

   /* factory<ScreenNavigator> {
        LegacyNavigator()
    }*/
}



val appModule = listOf(appKoinModule)
