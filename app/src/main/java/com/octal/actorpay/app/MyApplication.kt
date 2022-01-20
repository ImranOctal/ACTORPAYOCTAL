package com.octal.actorpay.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.octal.actorpay.BuildConfig
import com.octal.actorpay.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin



class MyApplication : MultiDexApplication() {


    lateinit var context: Context


    /**
     * onCreate
     *
     */
    override fun onCreate() {
        super.onCreate()

        context = this
        startKoin {
            // use Koin logger
            androidLogger(if (BuildConfig.DEBUG) org.koin.core.logger.Level.ERROR else org.koin.core.logger.Level.NONE)
//            printLogger()
            // declare used Android context
            androidContext(context)
            // declare modules
            modules(appModule)
        }
    }

    /*fun initFirebase() {
        FirebaseApp.initializeApp(this)
        //initFirebase()
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("FCM_reg_tokenFailed", task.exception.toString())
                return@OnCompleteListener
            }
            try {
                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = token
                Log.d("TAG", msg!!.toString())
                SharedPreferenceUtility.getInstance().save(Constant.FIRE_BASE_TOKEN, token)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
    }*/



}