package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.R
import com.octal.actorpay.Utils.CommonDialogsUtils
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentProfileBottomBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.bottomfragments.ProfileReesponse
import com.octal.actorpay.ui.dashboard.bottomnavfragments.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.Exception

class ProfileBottomFragment : BaseFragment() {
    private var _binding: FragmentProfileBottomBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel.getProfile()
        init()
        apiResponse()
        return root
        // Inflate the layout for this fragment
    }

    fun init() {
        binding.apply {
            binding.profileSave.setOnClickListener {
                //NavController().navigateWithId(R.id.homeFragment, findNavController())
                validate()
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
        } else if (binding.mobNumber.text.toString().trim().length != 10) {
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
        } else {
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

    fun saveProfile() {
        val email=binding.editEmail.text.toString().trim()
        val contactNumber=binding.mobNumber.text.toString().trim()
        val ext=binding.profileCcp.selectedCountryCodeWithPlus
        profileViewModel.saveProfile(email,ext,contactNumber)
    }

    fun apiResponse() {
        lifecycleScope.launch {
            profileViewModel.profileResponseLive.collect {
                when (it) {

                    is ProfileViewModel.ResponsProfileSealed.loading -> {
                        profileViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ProfileViewModel.ResponsProfileSealed.Success -> {
                        profileViewModel.methodRepo.hideLoadingDialog()
                        if (it.response is ProfileReesponse) {
                            val reesponse2=it.response.data
                            binding.firstName.setText(reesponse2.firstName + reesponse2.lastName)
                            binding.editEmail.setText(reesponse2.email)
                            binding.mobNumber.setText(reesponse2.contactNumber)
                            try {
                                var extContact = reesponse2.extensionNumber
                                if (extContact.isNotEmpty()) {
                                    extContact = extContact.replace("+", "")
                                    binding.profileCcp.setCountryForPhoneCode(extContact.toInt())
                                }
                            } catch (e: Exception) {
                                Log.d("Profile Fragment", "apiResponse: ${e.message}")
                            }
                        }
                        else if(it.response is SuccessResponse){
                            CommonDialogsUtils.showCommonDialog(requireActivity(),profileViewModel.methodRepo,"Profile Update",it.response.message)
                        }

                        else {
                            showCustomAlert(
                                getString(R.string.please_try_after_sometime),
                                binding.root
                            )
                        }
                    }
                    is ProfileViewModel.ResponsProfileSealed.ErrorOnResponse -> {
                        profileViewModel.methodRepo.hideLoadingDialog()
                        (requireActivity() as BaseActivity).showCustomAlert(
                            it.failResponse!!.message,
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun WorkStation() {

    }
}