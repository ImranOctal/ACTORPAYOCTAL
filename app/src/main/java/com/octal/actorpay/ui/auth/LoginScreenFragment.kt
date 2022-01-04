package com.octal.actorpay.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.LoginScreenFragmentBinding
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpay.utils.CommonDialogsUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class LoginScreenFragment : BaseFragment() {
    override fun WorkStation() {

    }

    private val loginViewModel: LoginViewModel by inject()
    private var _binding: LoginScreenFragmentBinding? = null

    private val binding get() = _binding!!
    private var showPassword = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LoginScreenFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        apiResponse()
        return root
    }

    private fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginResponseLive.collect { event ->
                when (event) {
                    is LoginViewModel.ResponseLoginSealed.loading -> {
                        loginViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is LoginViewModel.ResponseLoginSealed.Success -> {
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

                                (requireActivity() as BaseActivity).showCustomAlert(
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
                    is LoginViewModel.ResponseLoginSealed.ErrorOnResponse -> {
                        loginViewModel.methodRepo.hideLoadingDialog()
                        if (event.message!!.message.equals("Use account is not verified")) {
                            resendOtpUI()
                        } else
                            (requireActivity() as BaseActivity).showCustomAlert(
                                event.message!!.message,
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

    fun resendOtpUI() {
        CommonDialogsUtils.showCommonDialog(
            requireActivity(),
            loginViewModel.methodRepo,
            "Resend OTP",
            "Your Account is not verified.\nResend OTP on email?",
            true,
            true,
            true,
            false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    loginViewModel.resendOtp(binding.name.text.toString().trim())
                }

                override fun onCancel() {

                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun init() {
        binding.apply {
            buttonLogin.setOnClickListener {
                validateLogin()
            }
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
            binding.errorOnName.visibility = View.VISIBLE
            binding.errorOnName.text = getString(R.string.email_empty)
            binding.errorOnPassword.visibility = View.GONE
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginEmaillay,
                R.drawable.btn_search_outline
            )
        } else if (!loginViewModel.methodRepo.isValidEmail(
                binding.name.text.toString().trim()
            )
        ) {
            binding.errorOnName.visibility = View.VISIBLE
            binding.errorOnName.text = getString(R.string.invalid_email)
            binding.errorOnPassword.visibility = View.GONE
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginEmaillay,
                R.drawable.btn_search_outline
            )
        } else if (binding.password.text.toString().trim().isEmpty()) {
            binding.errorOnPassword.visibility = View.VISIBLE
            binding.errorOnPassword.text = getString(R.string.oops_your_password_is_empty)
            binding.errorOnName.visibility = View.GONE
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginEmaillay,
                R.drawable.btn_outline_gray
            )
            loginViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.loginPasslay,
                R.drawable.btn_search_outline
            )
        } else if (!binding.cbRememberMe.isChecked) {
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
        loginViewModel.SignInNow(
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