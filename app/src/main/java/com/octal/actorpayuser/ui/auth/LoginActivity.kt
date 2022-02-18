package com.octal.actorpayuser.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.octal.actorpayuser.MainActivity
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentLoginBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpayuser.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject


class LoginActivity : BaseActivity() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by inject()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

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

    private fun configSocialLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        callbackManager = CallbackManager.Factory.create()
        registerFbCallback()
    }


    fun init() {
        binding.apply {
            val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
            val viewPager = customViewPager as ViewPager
            viewPager.adapter = viewPagerAdapter
            tabs.setupWithViewPager(viewPager)
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                            viewPager.invalidate()
                            viewPagerAdapter.notifyDataSetChanged()
                        }
                        1 -> {
                            viewPager.invalidate()
                            viewPagerAdapter.notifyDataSetChanged()

                        }
                    }
                }
            })
            if (LoginViewModel.isFromContentPage)
                viewPager.currentItem = 1

            imGoogle.setOnClickListener {
                val intent = mGoogleSignInClient.signInIntent
                googleResultLauncher.launch(intent)
            }
            imFacebook.setOnClickListener {
                LoginManager.getInstance().logInWithReadPermissions(
                    this@LoginActivity,
                    callbackManager,
                    listOf("public_profile", "email")
                )
            }
        }


    }

    private fun apiResponse() {
        lifecycleScope.launch {
            loginViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        //(requireActivity() as BaseActivity).showLoading(true)
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        hideLoadingDialog()
                        when (event.response) {
                            is LoginResponses -> {
                                loginViewModel.methodRepo.dataStore.setUserId(event.response.data.id)
                                loginViewModel.methodRepo.dataStore.setIsLoggedIn(true)
                                loginViewModel.methodRepo.dataStore.setEmail(event.response.data.email)
                                loginViewModel.methodRepo.dataStore.setFirstName(event.response.data.firstName)
                                loginViewModel.methodRepo.dataStore.setLastName(event.response.data.lastName)
                                loginViewModel.methodRepo.dataStore.setAccessToken(event.response.data.access_token)
                                loginViewModel.methodRepo.dataStore.setRefreshToken(event.response.data.refresh_token)
                                showCustomAlert(
                                    "Logged in Successfully",
                                    binding.root
                                )
                                delay(1000)
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finishAffinity()
                            }
                            is String -> {
                                CommonDialogsUtils.showCommonDialog(
                                    this@LoginActivity,
                                    loginViewModel.methodRepo,
                                    "Forgot password",
                                    event.response
                                )
                            }
                            else -> {
                                showCustomAlert(
                                    getString(R.string.please_try_after_sometime),
                                    binding.root
                                )
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoadingDialog()
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


    private var googleResultLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
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
            val image = if (account.photoUrl == null)
                ""
            else
                account.photoUrl!!.toString()
            socialLogin(account.displayName!!, "", "google", account.email!!, account.id!!, image)

        } catch (e: ApiException) {

            showCustomAlert(
                e.message,
                binding.root
            )

        }
    }

    private fun registerFbCallback() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult) {

                    val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                                val json = response!!.jsonObject
                                try {
                                    if (json != null) {
                                        Log.d("fb response", json.toString())
                                        val email: String
                                        try {
                                            email = json.getString("email")

                                        } catch (e: Exception) {
                                            showCustomToast("Sorry!!! Your email is not verified on facebook.")
                                            return
                                        }
                                        val facebookId = json.getString("id")
                                        val firstName = json.getString("first_name")
                                        val lastName = json.getString("last_name")
//                                val name = json.getString("name")
                                        val picture =
                                            "https://graph.facebook.com/$facebookId/picture?type=large"
                                        Log.d("fb response", " picture$picture")

                                        LoginManager.getInstance().logOut()
                                        socialLogin(
                                            firstName,
                                            lastName,
                                            "fb",
                                            email,
                                            facebookId,
                                            picture
                                        )

                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    Log.d("fb response problem", "problem" + e.message)
                                }
                            }
                        })
                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id,name,first_name,last_name,link,email,picture"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException) {

                }

            })
    }

    private fun socialLogin(
        firstName: String,
        lastName: String,
        login_type: String,
        email: String,
        socialId: String,
        imgUrl: String
    ) {
        loginViewModel.socialLogin(firstName, lastName, login_type, email, socialId, imgUrl)
    }

}