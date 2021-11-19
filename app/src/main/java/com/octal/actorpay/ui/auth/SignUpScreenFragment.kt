package com.octal.actorpay.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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


class SignUpScreenFragment : BaseFragment() {

    private var _binding: SignUpScreenFragmentBinding? = null
    private val signupViewModel: SignupViewModel by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var showPassword=false
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
            signupPasswordShowHide.setOnClickListener {
                if(showPassword)
                {
                    password.transformationMethod = PasswordTransformationMethod()
                    showPassword=false
                }
                else{
                    password.transformationMethod = null
                    showPassword=true
                }
            }
            signupViewModel.methodRepo.makeTextLink(signipTermsPrivacy,"Terms of Use",true,null){
                showCustomToast("terms")
            }
            signupViewModel.methodRepo.makeTextLink(signipTermsPrivacy,"Privacy Policy",true,null){
                showCustomToast("privacy")
            }
        }
    }


    fun validate(){
        if (binding.editTextMobile.text.toString().trim().length<6) {
            binding.errorOnPhone.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_search_outline)
        }
        else if(binding.editTextMobile.text.toString().trim()[0].toString() == "0")
        {
            binding.errorOnPhone.visibility = View.VISIBLE
            binding.errorOnPhone.text=getString(R.string.mobile_not_start_with_0)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_search_outline)
        }
        else if (binding.firstName.text.toString().trim().isEmpty()) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_search_outline)
        }
        else if (binding.lastName.text.toString().trim().length<3) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnLastName.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_search_outline)
        }
        else if (binding.email.text.toString().length<3 || !signupViewModel.methodRepo.isValidEmail(binding.email.text.toString())) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnLastName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_search_outline)
        }
        else if (binding.password.text.toString().trim().length<8) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnLastName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.GONE
            binding.errorOnPassword.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_search_outline)
        }
        else if (!signupViewModel.methodRepo.isValidPassword(binding.password.text.toString().trim())) {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnLastName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.GONE
            binding.errorOnPassword.text = getString(R.string.oops_your_password_is_not_valid2)
            binding.errorOnPassword.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_search_outline)
        }
        else if(!binding.signCheckTerms.isChecked){

            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnLastName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.GONE
            binding.errorOnPassword.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_outline_gray)

            showCustomToast("Please agree to our terms to sign up")
        }
        else {
            binding.errorOnPhone.visibility = View.GONE
            binding.errorOnName.visibility = View.GONE
            binding.errorOnLastName.visibility = View.GONE
            binding.errorOnEmail.visibility = View.GONE
            binding.errorOnPassword.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_outline_gray)

            val countryCode=binding.ccp.selectedCountryCodeWithPlus

            signupViewModel.methodRepo.hideSoftKeypad(requireActivity())
            signupViewModel.SignUpNow(
                binding.firstName.text.toString().trim(),
                binding.lastName.text.toString().trim(),
                binding.email.text.toString().trim(),
                countryCode,
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

                        CommonDialogsUtils.showCommonDialog(
                            requireActivity(),
                            signupViewModel.methodRepo,
                            "Signed Up",
                            event.response,
                            autoCancelable = false,
                            isCancelAvailable = false,
                            isOKAvailable = true,
                            showClickable = false,
                            callback = object : CommonDialogsUtils.DialogClick {
                                override fun onClick() {
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            LoginActivity::class.java
                                        )
                                    )
                                    requireActivity().finishAffinity()
                                }

                                override fun onCancel() {

                                }
                            }
                        )
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