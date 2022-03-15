package com.octal.actorpayuser.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentSettingsBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.ui.misc.ChangePasswordDialog
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingViewModel: SettingViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiResponse()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)

        binding.changePassword.setOnClickListener {
            changePasswordUi()
        }
        binding.changePaymentOption.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.remittance)
        }
        binding.myAddress.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.shippingAddressFragment)
        }

        lifecycleScope.launchWhenCreated {
            settingViewModel.methodRepo.dataStore.isSocialLoggedIn().collect {
                if(it)
                    binding.changePassword.visibility=View.GONE
            }
        }

        return binding.root
    }

    private fun changePasswordUi(){
        ChangePasswordDialog().show(requireActivity(),settingViewModel.methodRepo){
                oldPassword, newPassword ->
            settingViewModel.changePassword(oldPassword,newPassword    )
        }
    }

    private fun apiResponse(){
        lifecycleScope.launch {
            settingViewModel.responseLive.collect {
                when(it){
                    is ResponseSealed.loading->{
                        showLoading()
                    }
                    is ResponseSealed.Success->{
                        hideLoading()
                        if(it.response is SuccessResponse){
                            CommonDialogsUtils.showCommonDialog(requireActivity(),settingViewModel.methodRepo,"Success",it.response.message)
                        }
                        else {
                            showCustomAlert(
                                getString(R.string.please_try_after_sometime),
                                binding.root
                            )
                        }
                    }
                    is ResponseSealed.ErrorOnResponse->{
                        hideLoading()
                        showCustomAlert(
                            it.message!!.message,
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

}