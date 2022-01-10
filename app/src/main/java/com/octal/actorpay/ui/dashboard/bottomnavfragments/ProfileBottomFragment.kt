package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.utils.CommonDialogsUtils
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentProfileBottomBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileReesponse
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileResponseData
import com.octal.actorpay.ui.auth.verifyotp.VerifyOtpDialog
import com.octal.actorpay.ui.dashboard.bottomnavfragments.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ProfileBottomFragment : BaseFragment() {
    private var _binding: FragmentProfileBottomBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by inject()



    companion object {
        private var instance: ProfileBottomFragment? = null

        @JvmStatic
        fun newInstance(): ProfileBottomFragment? {
            if (instance == null) {
                instance = ProfileBottomFragment()
            }
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiResponse()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel.getProfile()
        init()
        return root
        // Inflate the layout for this fragment
    }

    fun init() {
        (requireActivity() as MainActivity).title="My Profile"
        showHideBottomNav(true)
        showHideCartIcon(true)
        showHideFilterIcon(false)
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

                    is ProfileViewModel.ResponsProfileSealed.loading -> {
                        profileViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ProfileViewModel.ResponsProfileSealed.Success -> {
                        profileViewModel.methodRepo.hideLoadingDialog()
                        when (it.response) {
                            is ProfileReesponse -> {
                                val response2=it.response.data
                                handleProfileResponse(response2)
                            }
                            is SuccessResponse -> {
                                if(it.response.message.equals("OTP sent successfully"))
                                    VerifyOtpDialog().show(requireActivity(),profileViewModel.methodRepo){
                                            profileViewModel.verifyOtp(it)
                                    }
                                else if(it.response.message.equals("User contact number has been verified successfully"))
                                    profileViewModel.getProfile()
                                else
                                    CommonDialogsUtils.showCommonDialog(requireActivity(),profileViewModel.methodRepo,"Profile Update",it.response.message)
                            }
                            else -> {
                                showCustomAlert(
                                    getString(R.string.please_try_after_sometime),
                                    binding.root
                                )
                            }
                        }
                    }
                    is ProfileViewModel.ResponsProfileSealed.ErrorOnResponse -> {
                        profileViewModel.methodRepo.hideLoadingDialog()
                        if (it.failResponse!!.code == 403) {
                            forcelogout(profileViewModel.methodRepo)
                        }
                        else
                        (requireActivity() as BaseActivity).showCustomAlert(
                            it.failResponse.message,
                            binding.root
                        )
                    }
                    is ProfileViewModel.ResponsProfileSealed.Empty -> {
                        profileViewModel.methodRepo.hideLoadingDialog()
                    }
                }
            }
        }
    }

    fun handleProfileResponse(profileReesponse: ProfileResponseData){
        binding.firstName.setText("${profileReesponse.firstName} ${profileReesponse.lastName}")
        binding.editEmail.setText(profileReesponse.email)
        binding.mobNumber.setText(profileReesponse.extensionNumber+" "+profileReesponse.contactNumber)
        binding.gender.setText(profileReesponse.gender)
        binding.editAdhar.setText(profileReesponse.aadharNumber)
        binding.editPAN.setText(profileReesponse.panNumber)
        var outputDate=profileReesponse.dateOfBirth
                if(profileReesponse.dateOfBirth!=null && profileReesponse.dateOfBirth.equals("").not()){
                    val parser =  SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    val formatter =  SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                    try {
                     outputDate = formatter.format(parser.parse(profileReesponse.dateOfBirth)!!);
                    }
                    catch (e : Exception){

                    }
                }
        binding.dob.setText(outputDate)



        binding.mobileUpdate.visibility=View.GONE

        if(profileReesponse.phoneVerified){
            binding.verifyMobile.visibility=View.GONE
        }
        else{
            binding.verifyMobile.visibility=View.VISIBLE
        }
        if(profileReesponse.contactNumber == null || profileReesponse.contactNumber.equals("")){
            binding.mobileUpdate.text=getString(R.string.add_mobile_number)
            binding.verifyMobile.visibility=View.GONE
        }
        else{
            binding.mobileUpdate.text=getString(R.string.chnage_mobile_number)
        }
        try {
            var extContact = profileReesponse.extensionNumber
            if (extContact.isNotEmpty()) {
                extContact = extContact.replace("+", "")
                binding.profileCcp.setCountryForPhoneCode(extContact.toInt())
            }
        } catch (e: Exception) {
            Log.d("Profile Fragment", "apiResponse: ${e.message}")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun WorkStation() {

    }
}