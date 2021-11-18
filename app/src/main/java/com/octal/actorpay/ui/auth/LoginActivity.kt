package com.octal.actorpay.ui.auth

import android.R.attr
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.databinding.FragmentLoginBinding
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import org.koin.android.ext.android.inject
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Intent

import android.app.Activity

import androidx.activity.result.ActivityResultCallback

import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import android.R.attr.data
import android.util.Log
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.octal.actorpay.MainActivity
import com.octal.actorpay.Utils.CommonDialogsUtils
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class LoginActivity : BaseActivity() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by  inject()
    lateinit var mGoogleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding here
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_login)
    }
    override fun onResume() {
        super.onResume()
        init()
        apiResponse()
        configSocialLogin()
    }

    fun configSocialLogin(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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

            imGoogle.setOnClickListener {
                val intent =  mGoogleSignInClient.getSignInIntent();
                googleResultLauncher.launch(intent)
            }
        }


    }

    fun apiResponse() {
        lifecycleScope.launch {
            loginViewModel.loginResponseLive.collect { event ->
                when (event) {
                    is LoginViewModel.ResponseLoginSealed.loading -> {
                        //(requireActivity() as BaseActivity).showLoading(true)
                        loginViewModel.methodRepo.showLoadingDialog(this@LoginActivity)
                    }
                    is LoginViewModel.ResponseLoginSealed.Success -> {
                        loginViewModel.methodRepo.hideLoadingDialog()
                        if (event.response is LoginResponses) {
                            if (event.response != null && event.response.data != null) {
                                loginViewModel.sharedPre.setUserId(event.response.data.id)
                                loginViewModel.sharedPre.setIsregister(true)
                                loginViewModel.sharedPre.setIsFacebookLoggedIn(false)
                                loginViewModel.sharedPre.isGoogleLoggedIn = true;
                                loginViewModel.sharedPre.isLoggedIn = false;
                                loginViewModel.sharedPre.setUserEmail(event.response.data.email)
                                loginViewModel.sharedPre.setName(event.response.data.firstName)
                                loginViewModel.sharedPre.setJwtToken(
                                    event.response.data.access_token ?: ""
                                )
                                loginViewModel.sharedPre.setRefreshToken(
                                    event.response.data.refresh_token ?: ""
                                )
                                loginViewModel.sharedPre.setTokenType(
                                    event.response.data.token_type ?: ""
                                )
                                showCustomAlert(
                                    "Logged in Successfully",
                                    binding.root
                                )
                                delay(1000)
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finishAffinity()
                            }
                        } else if (event.response is String) {
                            loginViewModel.methodRepo.hideLoadingDialog()
                            CommonDialogsUtils.showCommonDialog(
                                this@LoginActivity,
                                loginViewModel.methodRepo,
                                "Forget Password",
                                event.response
                            )
                        } else {
                            loginViewModel.methodRepo.hideLoadingDialog()
                            showCustomAlert(
                                getString(R.string.please_try_after_sometime),
                                binding.root
                            )
                        }
                    }
                    is LoginViewModel.ResponseLoginSealed.ErrorOnResponse -> {
                        //(requireActivity() as BaseActivity).showLoading(false)
                        loginViewModel.sharedPre.setIsregister(false)
                        loginViewModel.sharedPre.setIsFacebookLoggedIn(false)
                        loginViewModel.sharedPre.isGoogleLoggedIn = false;
                        loginViewModel.sharedPre.isLoggedIn = false;
                        loginViewModel.sharedPre.setUserEmail("")
                        loginViewModel.sharedPre.setName("")
                        loginViewModel.sharedPre.setUserMobile("")
                        loginViewModel.sharedPre.setUserId("0")
                        loginViewModel.sharedPre.setName("")
                        loginViewModel.sharedPre.setJwtToken("")
                        loginViewModel.sharedPre.setRefreshToken("")
                        loginViewModel.sharedPre.setTokenType("")
                        showCustomAlert(
                            event.message!!.message,
                            binding.root
                        )
                    }
                    else -> Unit
                }
            }
        }
    }


    var googleResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
            mGoogleSignInClient.signOut()

        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            var image=""
                if(account.photoUrl==null)
                    image=""
                else
                    image=account.photoUrl!!.toString()
            socialLogin(account.getDisplayName()!!,"",account.email!!+System.currentTimeMillis(),account.id!!+System.currentTimeMillis(),image)

        } catch (e: ApiException) {

            showCustomAlert(
                e.message,
                binding.root
            )

        }
    }

    fun socialLogin(firstName:String,lastName:String,email:String,socialId:String,imgUrl:String){
            loginViewModel.socialLogin(firstName, lastName, email, socialId, imgUrl)
    }

}