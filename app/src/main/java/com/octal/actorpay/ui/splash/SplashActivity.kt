package com.octal.actorpay.ui.splash

//import android.content.pm.PackageManager
//import android.content.pm.PackageInfo
//import android.util.Base64
//import android.util.Log
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseCommonActivity
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.ActivitySplashScreenBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.auth.signup.SignupResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.CountryResponse
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.ui.intro.IntroActivity
import com.octal.actorpay.utils.CommonDialogsUtils
import com.octal.actorpay.utils.GlobalData
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


//import java.lang.Exception
//import java.security.MessageDigest
//import java.security.NoSuchAlgorithmException


class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: ActorPayViewModel by inject()
    var goTo=false

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
                showCustomToast("No Internet Available")
                finishAffinity()
            }

        }
    }

    fun gotoNextActivity(){

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.methodRepo.dataStore.isIntro().collect { isIntro ->
                viewModel.methodRepo.dataStore.isLoggedIn().collect { isLoggedIn ->
                    if (isIntro) {
                        if (isLoggedIn)
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        else
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finishAffinity()
                    } else {
                        startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                        finishAffinity()
                    }
                }

            }
        }


    }


    private fun apiResponse(){
        lifecycleScope.launch {
            viewModel.actorcResponseLive.collect {
                when(it){
                    is ActorPayViewModel.ResponseActorSealed.loading->{
//                        viewModel.methodRepo.showLoadingDialog(this@SplashActivity)
                    }
                    is ActorPayViewModel.ResponseActorSealed.Success->{
//                        viewModel.methodRepo.hideLoadingDialog()
                        if(it.response is SuccessResponse){
                            CommonDialogsUtils.showCommonDialog(this@SplashActivity,viewModel.methodRepo,"Success",it.response.message)
                        }
                        else if(it.response is CountryResponse){

                            GlobalData.allCountries.clear()
                            GlobalData.allCountries.addAll(it.response.data)
                            gotoNextActivity()

                        }
                        else {
                            showCustomAlert(
                                getString(R.string.please_try_after_sometime),
                                binding.root
                            )
                        }
                    }
                    is ActorPayViewModel.ResponseActorSealed.ErrorOnResponse->{
//                        viewModel.methodRepo.hideLoadingDialog()
                        showCustomAlert(
                            it.failResponse!!.message,
                            binding.root
                        )
                    }
                    is ActorPayViewModel.ResponseActorSealed.Empty -> {
//                        viewModel.methodRepo.hideLoadingDialog()
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