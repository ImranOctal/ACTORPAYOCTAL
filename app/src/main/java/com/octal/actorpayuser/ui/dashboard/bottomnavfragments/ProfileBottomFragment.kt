package com.octal.actorpayuser.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.octal.actorpayuser.R
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentProfileBottomBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.dateFormate1
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.dateFormate2
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.bottomfragments.ProfileResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.bottomfragments.ProfileResponseData
import com.octal.actorpayuser.ui.auth.verifyotp.VerifyOtpDialog
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.Exception

class ProfileBottomFragment : BaseFragment() {
    lateinit var binding: FragmentProfileBottomBinding
    private val profileViewModel: ProfileViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiResponse()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel.getProfile()
        init()
        return root
        // Inflate the layout for this fragment
    }

    fun init() {

        binding.apply {
          /*  profileSave.setOnClickListener {
                //NavController().navigateWithId(R.id.homeFragment, findNavController())
                validate()
            }*/
            verifyMobile.setOnClickListener {
                profileViewModel.sendOtp()
            }
        }
    }

    fun validate() {
        if (binding.editEmail.text.toString()
                .isEmpty() || !profileViewModel.methodRepo.isValidEmail(binding.editEmail.text.toString())
        ) {
            binding.profileErroronemail.visibility = View.VISIBLE
            profileViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.profileEmail,
                R.drawable.btn_search_outline
            )
        } else if (binding.mobNumber.text.toString().trim().length <=6) {
            binding.profileErroronemail.visibility = View.GONE
            binding.profileErroronphone.visibility = View.VISIBLE
            profileViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.profileEmail,
                R.drawable.btn_outline_gray
            )
            profileViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.profileNumber,
                R.drawable.btn_search_outline
            )
        }
        else if (binding.mobNumber.text.toString().trim()[0].toString() == "0"){
            binding.profileErroronemail.visibility = View.GONE
            binding.profileErroronphone.visibility = View.VISIBLE
            binding.profileErroronphone.text=getString(R.string.mobile_not_start_with_0)
            profileViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.profileEmail,
                R.drawable.btn_outline_gray
            )
            profileViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.profileNumber,
                R.drawable.btn_search_outline
            )
        }else {
            binding.profileErroronemail.visibility = View.GONE
            binding.profileErroronphone.visibility = View.GONE
            profileViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.profileEmail,
                R.drawable.btn_outline_gray
            )
            profileViewModel.methodRepo.setBackGround(
                requireContext(),
                binding.profileNumber,
                R.drawable.btn_outline_gray
            )
            saveProfile()
        }
    }

    private fun saveProfile() {
        val email=binding.editEmail.text.toString().trim()
        val contactNumber=binding.mobNumber.text.toString().trim()
        val ext=binding.profileCcp.selectedCountryCodeWithPlus
        profileViewModel.saveProfile(email,ext,contactNumber)
    }

    private fun apiResponse() {
        lifecycleScope.launch {
            profileViewModel.profileResponseLive.collect {
                when (it) {

                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (it.response) {
                            is ProfileResponse -> {
                                val response2=it.response.data
                                handleProfileResponse(response2)
                            }
                            is SuccessResponse -> {
                                when (it.response.message) {
                                    "OTP sent successfully" -> VerifyOtpDialog().show(requireActivity(),profileViewModel.methodRepo){
                                        otp->
                                        profileViewModel.verifyOtp(otp)
                                    }
                                    "User contact number has been verified successfully" -> profileViewModel.getProfile()
                                    else -> CommonDialogsUtils.showCommonDialog(requireActivity(),profileViewModel.methodRepo,"Profile Update",it.response.message)
                                }
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
                        if (it.message!!.code == 403) {
                            forcelogout(profileViewModel.methodRepo)
                        }
                        else
                        (requireActivity() as BaseActivity).showCustomAlert(
                            it.message.message,
                            binding.root
                        )
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun handleProfileResponse(profileResponse: ProfileResponseData){
        binding.nameTextView.setText("${profileResponse.firstName} ${profileResponse.lastName}")
        binding.firstName.setText("${profileResponse.firstName} ${profileResponse.lastName}")
        binding.editEmail.setText(profileResponse.email)
        if (profileResponse.extensionNumber!=null)
        binding.mobNumber.setText(profileResponse.extensionNumber.plus(" ").plus(profileResponse.contactNumber))
        binding.gender.setText(profileResponse.gender)
        binding.editAdhar.setText(profileResponse.aadharNumber)
        binding.editPAN.setText(profileResponse.panNumber)
        var outputDate=profileResponse.dateOfBirth
                if(profileResponse.dateOfBirth!=null && (profileResponse.dateOfBirth == "").not()){
                    try {
                     outputDate = dateFormate2.format(dateFormate1.parse(profileResponse.dateOfBirth)!!)
                    }
                    catch (e : Exception){

                    }
                }
        binding.dob.setText(outputDate)



        binding.mobileUpdate.visibility=View.GONE

        if(profileResponse.phoneVerified){
            binding.verifyMobile.visibility=View.GONE
        }
        else{
            binding.verifyMobile.visibility=View.VISIBLE
        }
        if(profileResponse.contactNumber == null || profileResponse.contactNumber == ""){
            binding.mobileUpdate.text=getString(R.string.enter_mobile_number)
            binding.verifyMobile.visibility=View.GONE
        }
        else{
            binding.mobileUpdate.text=getString(R.string.chnage_mobile_number)
        }
        try {
            var extContact = profileResponse.extensionNumber
            if (extContact.isNotEmpty()) {
                extContact = extContact.replace("+", "")
                binding.profileCcp.setCountryForPhoneCode(extContact.toInt())
            }
        } catch (e: Exception) {
            Log.d("Profile Fragment", "apiResponse: ${e.message}")
        }
    }


}