package com.octal.actorpay.ui.auth

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.databinding.FragmentLoginBinding
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import org.koin.android.ext.android.inject


class LoginActivity : BaseActivity() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by  inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding here
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_login)
    }
    override fun onResume() {
        super.onResume()
        init()
    }



    fun init() {
        binding.apply {
            val viewPagerAdapter =  ViewPagerAdapter(supportFragmentManager)
            viewPager.adapter = viewPagerAdapter
            tabs.setupWithViewPager(viewPager)
            viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            viewPager.invalidate();
                            viewPagerAdapter?.notifyDataSetChanged()
                        }
                        1 -> {
                            viewPager.invalidate();
                            viewPagerAdapter?.notifyDataSetChanged()

                        }
                    }
                }
            })
        }


    }
}