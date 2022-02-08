package com.octal.actorpayuser.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.ActivityIntroBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.ui.auth.LoginActivity
import com.octal.actorpayuser.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpayuser.utils.SnapHelperOneByOne
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    private val introList= mutableListOf<IntroModel>()
    private val loginViewModel: LoginViewModel by  inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        feedList()
        setAdapter()

    }

    private fun feedList(){
        introList.add(IntroModel("Add Account to Manage",getString(R.string.dummy_text),R.drawable.intro_1))
        introList.add(IntroModel("Track your Activity",getString(R.string.dummy_text),R.drawable.intro_2))
        introList.add(IntroModel("Make online Payment",getString(R.string.dummy_text),R.drawable.intro_3))
        introList.add(IntroModel("Safely Withdraw",getString(R.string.dummy_text),R.drawable.intro_4))
        introList.add(IntroModel("Quick Transfer",getString(R.string.dummy_text),R.drawable.intro_5))
    }

    fun setAdapter(){

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.introRecyclerview.layoutManager = layoutManager
        val recyclerAdapter = IntroAdapter(introList){
            action,position->
            when(action){
                Clicks.Skip->{
                    goToLogin()
                }
                Clicks.Next->{
                    binding.introRecyclerview.smoothScrollToPosition(position+1)
                }
                Clicks.Prev->{
                    binding.introRecyclerview.smoothScrollToPosition(position-1)
                }
                Clicks.GetStart->{
                    goToLogin()
                }
                else->Unit
            }
        }
        binding.introRecyclerview.adapter = recyclerAdapter
        binding.indicator.attachToRecyclerView(binding.introRecyclerview)
        val helper: SnapHelper = SnapHelperOneByOne()
        helper.attachToRecyclerView(binding.introRecyclerview)
    }
    private fun goToLogin(){
        lifecycleScope.launch {
            loginViewModel.methodRepo.dataStore.setIsIntro(true)
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
            finishAffinity()
        }

    }

}



data class IntroModel(
    val text:String,
    val desc:String,
    val image:Int
)