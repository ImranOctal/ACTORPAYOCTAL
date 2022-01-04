package com.octal.actorpay.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.R
import com.octal.actorpay.utils.CommonDialogsUtils
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.SignUpScreenFragmentBinding
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpay.ui.auth.viewmodel.SignupViewModel
import com.octal.actorpay.ui.content.ContentActivity
import com.octal.actorpay.ui.content.ContentViewModel
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import java.util.*


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
                    signupPasswordShowHide.setImageResource(R.drawable.show)
                    password.setSelection(password.text.toString().length)
                }
                else{
                    password.transformationMethod = null
                    showPassword=true
                    signupPasswordShowHide.setImageResource(R.drawable.hide)
                    password.setSelection(password.text.toString().length)
                }
            }
            signupViewModel.methodRepo.makeTextLink(signipTermsPrivacy,"Terms of Use",true,null){
                ContentViewModel.type=3
                LoginViewModel.isFromContentPage=true
                startActivity(Intent(requireContext(),ContentActivity::class.java))

            }
            signupViewModel.methodRepo.makeTextLink(signipTermsPrivacy,"Privacy Policy",true,null){
                ContentViewModel.type=2
                startActivity(Intent(requireContext(),ContentActivity::class.java))
            }

            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.gender_array,
                android.R.layout.simple_spinner_item
            ).also {
                    adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.spinnerGender.adapter = adapter
            }
            signupDob.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)


                val dpd = DatePickerDialog(requireActivity(),  { view, yearR, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    binding.dob.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + yearR)

                }, year, month, day)
                dpd.show()
                dpd.getDatePicker().setMaxDate(Date().time)
                dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        }
    }


    fun validate(){
        var isValidate=true
        if (binding.editTextMobile.text.toString().trim().length<6) {
            isValidate=false
            binding.errorOnPhone.visibility = View.VISIBLE
            binding.errorOnPhone.text=getString(R.string.error_phone)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_search_outline)
        }
        else{
            if(binding.editTextMobile.text.toString().trim()[0].toString() == "0")
            {
                isValidate=false
                binding.errorOnPhone.visibility = View.VISIBLE
                binding.errorOnPhone.text=getString(R.string.mobile_not_start_with_0)
                signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_search_outline)
            }
            else{
                binding.errorOnPhone.visibility = View.GONE
                signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            }
        }
        if (binding.firstName.text.toString().trim().isEmpty()) {
            isValidate=false
            binding.errorOnName.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnName.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
        }
        if (binding.lastName.text.toString().trim().length<3) {
            isValidate=false
            binding.errorOnLastName.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnLastName.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_outline_gray)
        }
        if (binding.email.text.toString().length<3 || !signupViewModel.methodRepo.isValidEmail(binding.email.text.toString())) {
             isValidate=false
            binding.errorOnEmail.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnEmail.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
        }
        if (binding.password.text.toString().trim().length<8) {
           isValidate=false
            binding.errorOnPassword.visibility = View.VISIBLE
            binding.errorOnPassword.text = getString(R.string.oops_your_password_is_not_valid)
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnPassword.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_outline_gray)
        }
        if (!signupViewModel.methodRepo.isValidPassword(binding.password.text.toString().trim())) {
            isValidate=false
            binding.errorOnPassword.text = getString(R.string.oops_your_password_is_not_valid2)
            binding.errorOnPassword.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnPassword.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_outline_gray)
        }
        if(!binding.signCheckTerms.isChecked){
            isValidate=false
            showCustomToast("Please agree to our terms to sign up")
        }
        val genderPosition=binding.spinnerGender.selectedItemPosition
        if(genderPosition==0){
            isValidate=false
            binding.errorOnGender.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupGender, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnGender.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupGender, R.drawable.btn_outline_gray)
        }
        if(binding.dob.text.toString().trim().equals("")){
            isValidate=false
            binding.errorOnDate.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupDob, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnDate.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupDob, R.drawable.btn_outline_gray)
        }

        if(binding.adhar.text.toString().trim().length<16){
            isValidate=false
            binding.errorOnAdhar.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupAdhar, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnAdhar.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupAdhar, R.drawable.btn_outline_gray)
        }

        if(binding.pan.text.toString().trim().length<10){
            isValidate=false
            binding.errorOnPan.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPan, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnPan.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPan, R.drawable.btn_outline_gray)
        }

        if(isValidate){

            val countryCode=binding.ccp.selectedCountryCodeWithPlus

            signupViewModel.methodRepo.hideSoftKeypad(requireActivity())
            signupViewModel.SignUpNow(
                binding.firstName.text.toString().trim(),
                binding.lastName.text.toString().trim(),
                binding.email.text.toString().trim(),
                countryCode,
                binding.editTextMobile.text.toString().trim(),
                binding.password.text.toString(),
                if(genderPosition==1) "Male" else "Female",
                binding.dob.text.toString().trim(),
                binding.adhar.text.toString().trim(),
                binding.pan.text.toString().trim(),
            )
        }
    }


    private fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            signupViewModel.signInResponseLive.collect { event ->
                when (event) {
                    is SignupViewModel.ResponseSignupSealed.loading -> {
                       signupViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is SignupViewModel.ResponseSignupSealed.Success -> {
                        signupViewModel.methodRepo.hideLoadingDialog()
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
                        signupViewModel.methodRepo.hideLoadingDialog()
                        (requireActivity() as BaseActivity).showCustomAlert(
                            event.message!!.message,
                            binding.root
                        )
                    }
                    else -> {
                        signupViewModel.methodRepo.hideLoadingDialog()
                    }
                }
            }

        }
    }

}