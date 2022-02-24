package com.octal.actorpayuser.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.actorpay.merchant.utils.SingleClickListener
import com.octal.actorpayuser.R
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.SignUpScreenFragmentBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.signup.SignupResponse
import com.octal.actorpayuser.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpayuser.ui.auth.viewmodel.SignupViewModel
import com.octal.actorpayuser.ui.content.ContentActivity
import com.octal.actorpayuser.ui.content.ContentViewModel
import com.octal.actorpayuser.utils.GlobalData
import com.octal.actorpayuser.utils.countrypicker.CountryPicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.DecimalFormat
import java.util.*



class SignUpScreenFragment : BaseFragment() {

    lateinit var binding: SignUpScreenFragmentBinding
    private val signupViewModel: SignupViewModel by inject()


    private var showPassword=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiResponse()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = SignUpScreenFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }

    fun init() {
        binding.apply {

            buttonSignUp.setOnClickListener(object : SingleClickListener() {
                override fun performClick(v: View?) {
                    validate()
                }
            })
            firstNameInfo.setOnClickListener {
                signupViewModel.methodRepo.showPopUpWindow(binding.firstNameInfo,"Enter first name as per government\nrecords or bank")
            }
            lastNameInfo.setOnClickListener {
                signupViewModel.methodRepo.showPopUpWindow(binding.lastNameInfo,"Enter last name as per government\nrecords or bank")
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

            signupViewModel.methodRepo.makeTextLink(signipTermsPrivacy,"Terms of Use",false,ContextCompat.getColor(requireContext(),R.color.primary)){
                ContentViewModel.type=3
                LoginViewModel.isFromContentPage=true
                startActivity(Intent(requireContext(),ContentActivity::class.java))

            }
            signupViewModel.methodRepo.makeTextLink(signipTermsPrivacy,"Privacy Policy",false,ContextCompat.getColor(requireContext(),R.color.primary)){
                ContentViewModel.type=2
                startActivity(Intent(requireContext(),ContentActivity::class.java))
            }
            val codeList= mutableListOf<String>()

                GlobalData.allCountries.forEach {
                    val code=it.countryCode
                    codeList.add(code)
                }

            if(GlobalData.allCountries.size>0){
                binding.codePicker.text=GlobalData.allCountries[0].countryCode
            }
            binding.countryLayout.setOnClickListener {
                CountryPicker(requireContext(),requireActivity(),viewModel.methodRepo,GlobalData.allCountries){
                    binding.codePicker.text=GlobalData.allCountries[it].countryCode
                }.show()
            }

            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.gender_array,
                android.R.layout.simple_list_item_1
            ).also {
                    adapter ->
                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.spinnerAutocomplete.setAdapter(adapter)
            }
            binding.spinnerAutocomplete.setOnClickListener {
                signupViewModel.methodRepo.hideSoftKeypad(requireActivity())
            }
            binding.spinnerAutocomplete.setOnItemClickListener { _, _, _, _ ->
                binding.errorOnGender.visibility = View.GONE
                signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupGender2, R.drawable.btn_outline_gray)
            }


            dob.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                signupViewModel.methodRepo.hideSoftKeypad(requireActivity())


                val dpd = DatePickerDialog(requireActivity(),  { _, yearR, monthOfYear, dayOfMonth ->

                    val f =  DecimalFormat("00")
                    val dayMonth=f.format(dayOfMonth)
                     val monthYear=f.format(monthOfYear+1)

                    with(binding) {

                        dob.setText("$dayMonth-$monthYear-$yearR")
                    }
                    binding.errorOnDate.visibility = View.GONE
                    signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupDob, R.drawable.btn_outline_gray)

                }, year, month, day)
                dpd.show()
                dpd.datePicker.maxDate = Date().time
                dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            }
        }
    }


    fun validate(){
        var isValidate=true

        val countryCode=binding.codePicker.text.toString().trim()

        if(binding.adhar.text.toString().trim().length<12){
            isValidate=false
            binding.adhar.error=getString(R.string.enter_valid_adhar)
            binding.adhar.requestFocus()
        }
        else{
            binding.errorOnAdhar.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupAdhar, R.drawable.btn_outline_gray)
        }

        if(!signupViewModel.methodRepo.isValidPAN(binding.pan.text.toString().trim())){
            isValidate=false
            binding.pan.error=getString(R.string.please_valid_pan)
            binding.pan.requestFocus()
        }
        else{
            binding.errorOnPan.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPan, R.drawable.btn_outline_gray)
        }


        if(binding.dob.text.toString().trim() == ""){
            isValidate=false
            binding.errorOnDate.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupDob, R.drawable.btn_search_outline)
        }
        else{
            binding.errorOnDate.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupDob, R.drawable.btn_outline_gray)
        }
        if(binding.spinnerAutocomplete.text.toString().trim() == ""){
            isValidate=false
            binding.errorOnGender.visibility = View.VISIBLE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupGender2, R.drawable.btn_search_outline)
            binding.scrollView.smoothScrollTo(0,binding.spinnerAutocomplete.top)
        }
        else{
            binding.errorOnGender.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupGender2, R.drawable.btn_outline_gray)
        }
        if (binding.password.text.toString().trim().length<8) {
            isValidate=false
            binding.password.error=getString(R.string.oops_your_password_is_not_valid)
            binding.password.requestFocus()
        }
        else{
            if (binding.password.text.toString().trim().contains(" ") || !signupViewModel.methodRepo.isValidPassword(binding.password.text.toString().trim())) {
                isValidate=false
                binding.password.error = getString(R.string.oops_your_password_is_not_valid2)
                binding.password.requestFocus()
            }
            else{
                binding.errorOnPassword.visibility = View.GONE
                signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPassword, R.drawable.btn_outline_gray)
            }
        }

        if (binding.email.text.toString().length<3 || !signupViewModel.methodRepo.isValidEmail(binding.email.text.toString())) {
            isValidate=false
            binding.email.error=getString(R.string.oops_your_email_is_not_correct_or_empty)
            binding.email.requestFocus()
        }
        else{
            binding.errorOnEmail.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupEmail, R.drawable.btn_outline_gray)
        }

        if (binding.lastName.text.toString().trim().length<3) {
            isValidate=false
            binding.lastName.error=getString(R.string.error_l_name)
            binding.lastName.requestFocus()
        }
        else{
            binding.errorOnLastName.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupLast, R.drawable.btn_outline_gray)
        }

        if (binding.firstName.text.toString().trim().isEmpty()) {
            isValidate=false
            binding.firstName.error=getString(R.string.error_name)
            binding.firstName.requestFocus()
        }
        else{
            binding.errorOnName.visibility = View.GONE
            signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupFirst, R.drawable.btn_outline_gray)
        }

        if (binding.editTextMobile.text.toString().trim().length<7) {
            isValidate=false
            binding.editTextMobile.error=getString(R.string.error_phone)
            binding.editTextMobile.requestFocus()
        }
        else{
            if(binding.editTextMobile.text.toString().trim()[0].toString() == "0")
            {
                isValidate=false
                binding.editTextMobile.error=getString(R.string.mobile_not_start_with_0)
                binding.editTextMobile.requestFocus()
            }
            else{
                binding.errorOnPhone.visibility = View.GONE
                signupViewModel.methodRepo.setBackGround(requireContext(), binding.signupPhone, R.drawable.btn_outline_gray)
            }
        }
        if(!binding.signCheckTerms.isChecked){
            isValidate=false
            showCustomToast("Please agree to our terms to sign up")
        }
        if(isValidate){

//            val countryCode=binding.ccp.selectedCountryCodeWithPlus

            signupViewModel.methodRepo.hideSoftKeypad(requireActivity())
            signupViewModel.signUpNow(
                binding.firstName.text.toString().trim(),
                binding.lastName.text.toString().trim(),
                binding.email.text.toString().trim(),
                countryCode,
                binding.editTextMobile.text.toString().trim(),
                binding.password.text.toString(),
                binding.spinnerAutocomplete.text.toString().trim(),
                binding.dob.text.toString().trim(),
                binding.adhar.text.toString().trim(),
                binding.pan.text.toString().trim(),
            )
        }
    }


    private fun apiResponse() {
        lifecycleScope.launch {
            signupViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                       showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when(event.response){
                            is SignupResponse->{
                                CommonDialogsUtils.showCommonDialog(
                                    requireActivity(),
                                    signupViewModel.methodRepo,
                                    "Signed Up",
                                    event.response.message,
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
                                signupViewModel.responseLive.value=ResponseSealed.Empty
                            }
                        }

                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        showCustomAlert(
                            event.message!!.message,
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

}