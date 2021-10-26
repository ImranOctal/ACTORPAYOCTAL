package com.octal.actorpay.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.databinding.ActivitySplashScreenBinding
import com.octal.actorpay.databinding.FragmentSplashBinding
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: ActorPayViewModel by  inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_splash_screen)
        lifecycleScope.launch (Dispatchers.Main){
            delay(2000L)
            startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
        }
    }
}