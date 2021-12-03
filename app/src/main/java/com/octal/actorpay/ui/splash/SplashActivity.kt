package com.octal.actorpay.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.databinding.ActivitySplashScreenBinding
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

//import android.content.pm.PackageManager
//import android.content.pm.PackageInfo
//import android.util.Base64
//import android.util.Log
import kotlinx.coroutines.flow.collect
//import java.lang.Exception
//import java.security.MessageDigest
//import java.security.NoSuchAlgorithmException


class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: ActorPayViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

//        printHashKey()
        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000L)
            viewModel.methodRepo.dataStore.isLoggedIn().collect { value ->
                if (value)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                else
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finishAffinity()
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