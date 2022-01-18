package com.octal.actorpay.di


import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.octal.actorpay.database.datastore.*
import com.octal.actorpay.di.models.CoroutineContextProvider
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.BASE_URL
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitMainRepository
import com.octal.actorpay.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpay.retrofitrepository.apiclient.ApiClient
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpay.ui.auth.viewmodel.SignupViewModel
import com.octal.actorpay.ui.cart.CartViewModel
import com.octal.actorpay.ui.content.ContentViewModel
import com.octal.actorpay.ui.dashboard.bottomnavfragments.viewmodels.ProfileViewModel
import com.octal.actorpay.ui.misc.MiscViewModel
import com.octal.actorpay.ui.myOrderList.OrderViewModel
import com.octal.actorpay.ui.myOrderList.orderdetails.OrderDetailsViewModel
import com.octal.actorpay.ui.myOrderList.placeorder.PlaceOrderViewModel
import com.octal.actorpay.ui.productList.ProductViewModel
import com.octal.actorpay.ui.productList.productdetails.ProductDetailsViewModel
import com.octal.actorpay.ui.productList.productsfilter.ProductFilterViewModel
import com.octal.actorpay.ui.promocodes.PromoListViewModel
import com.octal.actorpay.ui.settings.SettingViewModel
import com.octal.actorpay.ui.shippingaddress.ShippingAddressViewModel
import com.octal.actorpay.ui.shippingaddress.details.ShippingAddressDetailsViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val appKoinModule = module {


    single { CoroutineContextProvider() }

    single { DataStoreCoroutinesHandler }

    single<DataStoreBase> {
        DataStoreCustom(androidContext())
    }

    single {
        MethodsRepo(context = androidContext(), dataStore = get())
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
           /*  .addInterceptor(okhttp3.Interceptor {chain ->
               val request:Request=chain.request().newBuilder().build()
                 chain.proceed(request)
             })*/
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
   /* single<okhttp3.Interceptor> {
        okhttp3.Interceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("Authorization", "Bearer " + "token").build()
            chain.proceed(request)
        }
    }*/
    single<ApiClient> {
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiClient::class.java)
    }
    single<RetrofitRepository> {
        RetrofitMainRepository(androidContext(), apiClient = get())
    }

    viewModel {
        ActorPayViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        LoginViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        SignupViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        ProfileViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        MiscViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        ContentViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        ProductViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        ProductFilterViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        ProductDetailsViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        OrderViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        OrderDetailsViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        PromoListViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        PlaceOrderViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        ShippingAddressViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        ShippingAddressDetailsViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        SettingViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    single {
        CartViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }

}


val appModule = listOf(appKoinModule)
