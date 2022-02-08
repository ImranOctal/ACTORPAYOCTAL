package com.octal.actorpayuser.di


import com.octal.actorpayuser.viewmodel.ActorPayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.octal.actorpayuser.database.datastore.*
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.BASE_URL
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitMainRepository
import com.octal.actorpayuser.repositories.retrofitrepository.repo.RetrofitRepository
import com.octal.actorpayuser.repositories.retrofitrepository.apiclient.ApiClient
import com.octal.actorpayuser.ui.addmoney.AddMoneyViewModel
import com.octal.actorpayuser.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpayuser.ui.auth.viewmodel.SignupViewModel
import com.octal.actorpayuser.ui.cart.CartViewModel
import com.octal.actorpayuser.ui.content.ContentViewModel
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.viewmodels.ProfileViewModel
import com.octal.actorpayuser.ui.misc.MiscViewModel
import com.octal.actorpayuser.ui.mobileanddth.MobileAndDthViewModel
import com.octal.actorpayuser.ui.myOrderList.OrderViewModel
import com.octal.actorpayuser.ui.myOrderList.orderdetails.OrderDetailsViewModel
import com.octal.actorpayuser.ui.myOrderList.placeorder.PlaceOrderViewModel
import com.octal.actorpayuser.ui.notification.NotificationViewModel
import com.octal.actorpayuser.ui.productList.ProductViewModel
import com.octal.actorpayuser.ui.productList.productdetails.ProductDetailsViewModel
import com.octal.actorpayuser.ui.productList.productsfilter.ProductFilterViewModel
import com.octal.actorpayuser.ui.promocodes.PromoListViewModel
import com.octal.actorpayuser.ui.settings.SettingViewModel
import com.octal.actorpayuser.ui.shippingaddress.ShippingAddressViewModel
import com.octal.actorpayuser.ui.shippingaddress.details.ShippingAddressDetailsViewModel
import com.octal.actorpayuser.ui.transferMoney.TransferMoneyViewModel
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
    single {
        CartViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
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
    viewModel {
        AddMoneyViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        TransferMoneyViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        MobileAndDthViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }
    viewModel {
        NotificationViewModel(dispatcherProvider = get(), methodRepo = get(), apiRepo = get())
    }


}


val appModule = listOf(appKoinModule)
