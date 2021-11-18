package com.octal.actorpay.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.Utils.CommonDialogsUtils
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.LoginScreenFragmentBinding
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.ForgetPasswordParams
import com.octal.actorpay.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class LoginScreenFragment : BaseFragment() {
    override fun WorkStation() {

    }

    private val loginViewModel: LoginViewModel by inject()
    private var _binding: LoginScreenFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LoginScreenFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        ApiResponse()
        return root
    }

    private fun ApiResponse() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginResponseLive.collect { event ->
                when (event) {
                    is LoginViewModel.ResponseLoginSealed.loading -> {
                        //(requireActivity() as BaseActivity).showLoading(true)
                    }
                    is LoginViewModel.ResponseLoginSealed.Success -> {
                        if(event.response is LoginResponses){
                        if (event.response != null && event.response.data != null) {
                            loginViewModel.sharedPre.setUserId(event.response.data.id)
                            loginViewModel.sharedPre.setIsregister(true)
                            loginViewModel.sharedPre.setIsFacebookLoggedIn(false)
                            loginViewModel.sharedPre.isGoogleLoggedIn = false;
                            loginViewModel.sharedPre.isLoggedIn = true;
                            loginViewModel.sharedPre.setUserEmail(event.response.data.email)
                            loginViewModel.sharedPre.setName(event.response.data.firstName)
                            loginViewModel.sharedPre.setJwtToken(event.response.data.access_token ?: "")
                            loginViewModel.sharedPre.setRefreshToken(
                                event.response.data.refresh_token ?: ""
                            )
                            loginViewModel.sharedPre.setTokenType(event.response.data.token_type ?: "")
                            (requireActivity() as BaseActivity).showCustomAlert(
                                "Logged in Successfully",
                                binding.root
                            )
                            delay(1000)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finishAffinity()
                        }
                        }
                        else if(event.response is String){
                            CommonDialogsUtils.showCommonDialog(requireActivity(),loginViewModel.methodRepo,"Forget Password",event.response)
                        }
                        else {
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
                        (requireActivity() as BaseActivity).showCustomAlert(
                            event.message!!.message,
                            binding.root
                        )
                    }
                    else -> Unit
                }
            }

        }
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

        }
    }

    fun validateLogin() {
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
            binding.name.text.toString(),
            binding.password.text.toString()
        )
    }

    fun forgetPassword(){
        ForgetPasswordDialog().show(requireActivity(),loginViewModel.methodRepo){
            email ->
            loginViewModel.forgetPassword(email)
        }
    }

}