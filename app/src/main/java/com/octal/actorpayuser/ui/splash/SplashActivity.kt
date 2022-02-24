package com.octal.actorpayuser.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.octal.actorpayuser.MainActivity
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.ActivitySplashScreenBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.CountryResponse
import com.octal.actorpayuser.ui.auth.LoginActivity
import com.octal.actorpayuser.ui.intro.IntroActivity
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.utils.GlobalData
import com.octal.actorpayuser.viewmodel.ActorPayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: ActorPayViewModel by inject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        Firebase.messaging.isAutoInitEnabled = true
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, System.currentTimeMillis().toString())
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ActorPay")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
//        printHashKey()
        apiResponse()


        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000L)
            if(viewModel.methodRepo.isNetworkConnected()) {
                viewModel.getAllCountries()
            }
            else{
                CommonDialogsUtils.networkDialog(this@SplashActivity,viewModel.methodRepo){
                    viewModel.getAllCountries()
                }
            }
        }
    }

    private fun gotoNextActivity(){
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.methodRepo.dataStore.isIntro().collect { isIntro ->
                viewModel.methodRepo.dataStore.isLoggedIn().collect { isLoggedIn ->
                    viewModel.methodRepo.dataStore.getDeviceToken().collect { token ->
                        Log.d("FCM", "token: $token")
                        if (isIntro) {
                            if (isLoggedIn)
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            else
                                startActivity(
                                    Intent(
                                        this@SplashActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            finishAffinity()
                        } else {
                            startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                            finishAffinity()
                        }
                    }
                }

            }
        }


    }


    private fun apiResponse(){
        lifecycleScope.launch {
            viewModel.actorResponseLive.collect {
                when(it){
                    is ResponseSealed.loading->{
                    }
                    is ResponseSealed.Success->{
                        when (it.response) {
                            is SuccessResponse -> {
                                CommonDialogsUtils.showCommonDialog(this@SplashActivity,viewModel.methodRepo,"Success",it.response.message)
                            }
                            is CountryResponse -> {

                                GlobalData.allCountries.clear()
                                GlobalData.allCountries.addAll(it.response.data)
                                gotoNextActivity()

                            }
                            else -> {
                                showCustomAlert(
                                    getString(R.string.please_try_after_sometime),
                                    binding.root
                                )
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse->{
                        showCustomAlert(
                            it.message!!.message,
                            binding.root
                        )
                    }
                    is ResponseSealed.Empty -> {
                    }
                }
            }
        }
    }




    /*   fun printHashKey() {
           try {
               val info: PackageInfo = getPackageManager()
                   .getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES)
               for (signature in info.signatures) {
                   val md: MessageDigest = MessageDigest.getInstance("SHA")
                   md.update(signature.toByteArray())
                   val hashKey: String = String(Base64.encode(md.digest(), 0))
                   Log.i("Fb Key Hash", "printHashKey() Hash Key: $hashKey")
               }
           } catch (e: NoSuchAlgorithmException) {
               Log.e("Fb Key Hash", "printHashKey()", e)
           } catch (e: Exception) {
               Log.e("Fb Key Hash", "printHashKey()", e)
           }
       }*/
}