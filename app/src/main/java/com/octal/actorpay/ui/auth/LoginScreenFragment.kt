package com.octal.actorpay.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.actorpay.merchant.utils.SingleClickListener
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseCommonActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.LoginScreenFragmentBinding
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpay.utils.CommonDialogsUtils
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
                        loginViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ResponseSealed.Success -> {
                        loginViewModel.methodRepo.hideLoadingDialog()
                        when (event.response) {
                            is LoginResponses -> {

                                viewModel.methodRepo.dataStore.setUserId(event.response.data.id)
                                viewModel.methodRepo.dataStore.setIsLoggedIn(true)
                                viewModel.methodRepo.dataStore.setEmail(event.response.data.email)
                                viewModel.methodRepo.dataStore.setFirstName(event.response.data.firstName)
                                viewModel.methodRepo.dataStore.setLastName(event.response.data.lastName)
                                viewModel.methodRepo.dataStore.setAccessToken(event.response.data.access_token)
                                viewModel.methodRepo.dataStore.setRefreshToken(event.response.data.refresh_token)

                                (requireActivity() as BaseCommonActivity).showCustomAlert(
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
                        loginViewModel.methodRepo.hideLoadingDialog()
                        if (event.message!!.message == "Use account is not verified") {
                            resendOtpUI()
                        } else
                            (requireActivity() as BaseCommonActivity).showCustomAlert(
                                event.message.message,
                                binding.root
                            )
                    }
                    else -> {
                        loginViewModel.methodRepo.hideLoadingDialog()
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
                    loginViewModel.resendOtp(binding.name.text.toString().trim())
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

        }
    }

    private fun validateLogin() {
        if (binding.name.text.toString().trim().isEmpty()) {
            binding.name.error = getString(R.string.email_empty)
            binding.errorOnPassword.visibility = View.GONE
            binding.name.requestFocus()
        } else if (!loginViewModel.methodRepo.isValidEmail(
                binding.name.text.toString().trim()
            )
        ) {
            binding.name.error = getString(R.string.invalid_email)
            binding.errorOnPassword.visibility = View.GONE
            binding.name.requestFocus()
        } else if (binding.password.text.toString().trim().isEmpty()) {
            binding.password.error = getString(R.string.oops_your_password_is_empty)
            binding.errorOnName.visibility = View.GONE
            binding.password.requestFocus()
        }
        else if(!loginViewModel.methodRepo.isValidPassword(binding.password.text.toString().trim())){
            binding.password.error = getString(R.string.oops_your_password_is_not_valid2)
            binding.password.requestFocus()
        }
        else if (!binding.cbRememberMe.isChecked) {
            binding.errorOnName.visibility = View.GONE
            binding.errorOnPassword.visibility = View.GONE
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginEmaillay,
                R.drawable.btn_outline_gray
            )
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginPasslay,
                R.drawable.btn_outline_gray
            )
            login()
        } else {
            binding.errorOnName.visibility = View.GONE
            binding.errorOnPassword.visibility = View.GONE
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginEmaillay,
                R.drawable.btn_outline_gray
            )
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginPasslay,
                R.drawable.btn_outline_gray
            )
            login()
        }
    }

    fun login() {
        loginViewModel.methodRepo.hideSoftKeypad(requireActivity())
        loginViewModel.signInNow(
            binding.name.text.toString().trim(),
            binding.password.text.toString().trim()
        )
    }

    private fun forgetPassword() {
        ForgetPasswordDialog().show(requireActivity(), loginViewModel.methodRepo) { email ->
            loginViewModel.forgetPassword(email)
        }
    }

}