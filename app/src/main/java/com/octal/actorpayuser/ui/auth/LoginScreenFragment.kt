package com.octal.actorpayuser.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.actorpay.merchant.utils.SingleClickListener
import com.octal.actorpayuser.MainActivity
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.LoginScreenFragmentBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpayuser.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class LoginScreenFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by inject()
    lateinit var binding: LoginScreenFragmentBinding

    private var showPassword = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LoginScreenFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        apiResponse()
        return root
    }

    private fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {

                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is LoginResponses -> {

                                loginViewModel.methodRepo.dataStore.setUserId(event.response.data.id)
                                loginViewModel.methodRepo.dataStore.setIsLoggedIn(true)
                                loginViewModel.methodRepo.dataStore.setIsSocialLoggedIn(false)
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
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                                requireActivity().finishAffinity()
                            }
                            is String -> {
                                CommonDialogsUtils.showCommonDialog(
                                    requireActivity(),
                                    loginViewModel.methodRepo,
                                    "Resend Activation Link",
                                    event.response
                                )
                                loginViewModel.responseLive.value=ResponseSealed.Empty
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
                        hideLoading()
                        if (event.message!!.message == "User account is not verified") {
                            resendOtpUI()
                        } else
                            showCustomAlert(
                                event.message.message,
                                binding.root
                            )
                    }
                    else -> {
                     hideLoading()
                    }
                }
            }

        }
    }

    private fun resendOtpUI() {
        CommonDialogsUtils.showCommonDialog(
            requireActivity(),
            loginViewModel.methodRepo,
            "Resend OTP",
            "Your Account is not verified.\nResend OTP on email?",
            autoCancelable = true,
            isCancelAvailable = true,
            isOKAvailable = true,
            showClickable = false,
            callback = object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    loginViewModel.resendOtp(binding.loginEmail.text.toString().trim())
                }

                override fun onCancel() {

                }
            })
    }

    fun init() {
        binding.apply {
            /*buttonLogin.setOnClickListener {
                validateLogin()
            }*/
            buttonLogin.setOnClickListener(object : SingleClickListener() {
                override fun performClick(v: View?) {
                    validateLogin()
                }
            })

            loginForget.setOnClickListener {
                forgetPassword()
            }
            passwordShowHide.setOnClickListener {
                if (showPassword) {
                    password.transformationMethod = PasswordTransformationMethod()
                    showPassword = false
                    passwordShowHide.setImageResource(R.drawable.show)
                    password.setSelection(password.text.toString().length)
                } else {
                    password.transformationMethod = null
                    showPassword = true
                    passwordShowHide.setImageResource(R.drawable.hide)
                    password.setSelection(password.text.toString().length)
                }
            }

            loginEmail.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty() || loginViewModel.methodRepo.isValidEmail(text.toString().trim())) {
                    errorOnEmail.visibility = View.GONE
                } else{
                    errorOnEmail.visibility = View.VISIBLE
                }
            }
            password.doOnTextChanged { text, start, before, count ->

                val password = text.toString()
                var temp = ""
                if (!loginViewModel.methodRepo.isSpecialCharacter(password))
                    temp = getString(R.string.error_password_special)
                if (!loginViewModel.methodRepo.isDigit(password))
                    temp = getString(R.string.error_password_digit)
                if (!loginViewModel.methodRepo.isSmallLetter(password))
                    temp = getString(R.string.error_password_small)
                if (!loginViewModel.methodRepo.isCapitalLetter(password))
                    temp = getString(R.string.error_password_capital)
                if (password.length < 8)
                    temp = getString(R.string.oops_your_password_is_not_valid)


                if (temp!="" && password.length != 0) {
                    errorOnPassword.visibility = View.VISIBLE
                    errorOnPassword.text = temp
                } else {
                    errorOnPassword.visibility = View.GONE
                    errorOnPassword.text = ""
                }
            }

        }
    }



    private fun validateLogin() {

        var isValid=true

        if (binding.password.text.toString().trim().isEmpty() || !loginViewModel.methodRepo.isValidPassword(binding.password.text.toString().trim())) {
            binding.password.error = getString(R.string.oops_your_password_is_not_valid2)
            binding.password.requestFocus()
            isValid=false
        }

        if (binding.loginEmail.text.toString().trim().isEmpty() || !loginViewModel.methodRepo.isValidEmail(binding.loginEmail.text.toString().trim())) {
            binding.loginEmail.error = getString(R.string.invalid_email)
            binding.loginEmail.requestFocus()
            isValid=false
        }


        loginViewModel.methodRepo.hideSoftKeypad(requireActivity())
         if(isValid) {

            login()
        }
    }

    fun login() {
        loginViewModel.methodRepo.hideSoftKeypad(requireActivity())
        loginViewModel.signInNow(
            binding.loginEmail.text.toString().trim(),
            binding.password.text.toString().trim()
        )
    }

    private fun forgetPassword() {
        ForgetPasswordDialog().show(requireActivity(), loginViewModel.methodRepo) { email ->
            loginViewModel.forgetPassword(email)
        }
    }

}