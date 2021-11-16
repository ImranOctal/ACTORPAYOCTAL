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
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.databinding.LoginScreenFragmentBinding
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class LoginScreenFragment : Fragment() {
    private val viewModel: LoginViewModel by  inject()
    private var _binding: LoginScreenFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LoginScreenFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        ApiResponse()
        return root
    }

    private fun ApiResponse() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginResponseLive.collect { event ->
                when (event) {
                    is LoginViewModel.ResponseLoginSealed.loading -> {
                        //(requireActivity() as BaseActivity).showLoading(true)
                    }
                    is LoginViewModel.ResponseLoginSealed.Success -> {
                        if(event.response!=null && event.response.data!=null){
                            viewModel.sharedPre.setUserId(event.response.data.id)
                            viewModel.sharedPre.setIsregister(true)
                            viewModel.sharedPre.setIsFacebookLoggedIn(false)
                            viewModel.sharedPre.isGoogleLoggedIn = false;
                            viewModel.sharedPre.isLoggedIn = true;
                            viewModel.sharedPre.setUserEmail(event.response.data.email)
                            viewModel.sharedPre.setName(event.response.data.firstName)
                            viewModel.sharedPre.setJwtToken(event.response.data.access_token?:"")
                            viewModel.sharedPre.setRefreshToken(event.response.data.refresh_token?:"")
                            viewModel.sharedPre.setTokenType(event.response.data.token_type?:"")
                            (requireActivity() as BaseActivity).showCustomAlert("Logged in Successfully", binding.root)
                            delay(1000)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finishAffinity()
                        }else{
                            (requireActivity() as BaseActivity).showCustomAlert(getString(R.string.please_try_after_sometime), binding.root)
                        }
                    }
                    is LoginViewModel.ResponseLoginSealed.ErrorOnResponse -> {
                        //(requireActivity() as BaseActivity).showLoading(false)
                        viewModel.sharedPre.setIsregister(false)
                        viewModel.sharedPre.setIsFacebookLoggedIn(false)
                        viewModel.sharedPre.isGoogleLoggedIn = false;
                        viewModel.sharedPre.isLoggedIn = false;
                        viewModel.sharedPre.setUserEmail("")
                        viewModel.sharedPre.setName("")
                        viewModel.sharedPre.setUserMobile("")
                        viewModel.sharedPre.setUserId("0")
                        viewModel.sharedPre.setName("")
                        viewModel.sharedPre.setJwtToken("")
                        viewModel.sharedPre.setRefreshToken("")
                        viewModel.sharedPre.setTokenType("")
                        (requireActivity() as BaseActivity). showCustomAlert(event.message, binding.root)
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
    fun init(){
        binding.apply {
            buttonLogin.setOnClickListener {
             /*   //NavController().navigateWithId(R.id.homeFragment,findNavController())
              */
                viewModel.SignInNow(
                    binding.name.text.toString(),
                    binding.password.text.toString()
                )
            }
        }
    }
}