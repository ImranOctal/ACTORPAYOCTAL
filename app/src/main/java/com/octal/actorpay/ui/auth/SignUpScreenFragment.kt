package com.octal.actorpay.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.R
import com.octal.actorpay.Utils.CommonDialogsUtils
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.SignUpScreenFragmentBinding
import com.octal.actorpay.ui.auth.viewmodel.SignupViewModel
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 * Use the [LoginActivity.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpScreenFragment : BaseFragment() {

    private var _binding: SignUpScreenFragmentBinding? = null
    private val signupViewModel: SignupViewModel by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun WorkStation() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = SignUpScreenFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        apiResponse()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun init() {
        binding.apply {
            buttonSignUp.setOnClickListener {
                //NavController().navigateWithId(R.id.homeFragment, findNavController())
              validate()
            }
        }
    }


    fun validate(){
        if (binding.editTextMobile.text.toString().trim().length!=10) {
            binding.errorOnPhone.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_search_outline)
        }
        else if (binding.firstName.text.toString().trim().isEmpty()) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_search_outline)
        }
        else if (binding.email.text.toString().isEmpty() || !signupViewModel.methodRepo.isValidEmail(binding.email.text.toString())) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_search_outline)
        }
        else if (binding.password.text.toString().trim().length<8) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.GONE
            binding.errorOnPassword.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_search_outline)
        }
        else {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.GONE
            binding.errorOnPassword.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_outline_gray)

            val countrycode=binding.ccp.selectedCountryCodeWithPlus

            signupViewModel.methodRepo.hideSoftKeypad(requireActivity())
            signupViewModel.SignUpNow(
                binding.firstName.text.toString().trim(),
                binding.lastName.text.toString().trim(),
                binding.email.text.toString().trim(),
                countrycode,
                binding.editTextMobile.text.toString().trim(),
                binding.password.text.toString()
            )
        }
    }


    private fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            signupViewModel.signInResponseLive.collect { event ->
                when (event) {
                    is SignupViewModel.ResponseSignupSealed.loading -> {
                        //(requireActivity() as BaseActivity).showLoading(true)
                    }
                    is SignupViewModel.ResponseSignupSealed.Success -> {

                        CommonDialogsUtils.showCommonDialog(requireActivity(),signupViewModel.methodRepo,"Signed Up",event.response,false,false,true,false,object :CommonDialogsUtils.DialogClick{
                            override fun onClick() {
                                startActivity(Intent(requireContext(), LoginActivity::class.java))
                                requireActivity().finishAffinity()
                            }
                            override fun onCancel() {

                            }
                        })
                    }
                    is SignupViewModel.ResponseSignupSealed.ErrorOnResponse -> {

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

}