package com.octal.actorpay.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.FragmentSettingsBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.ui.misc.ChangePasswordDialog
import com.octal.actorpay.ui.misc.MiscViewModel
import com.octal.actorpay.ui.remittance.RemittanceFragment
import com.octal.actorpay.ui.shippingaddress.ShippingAddressFragment
import com.octal.actorpay.utils.CommonDialogsUtils
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingViewModel: SettingViewModel by inject()
    override fun WorkStation() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiResponse()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)

//        setTitle(getString(R.string.settings))
        showHideBottomNav(false)
        showHideCartIcon(false)
        showHideFilterIcon(false)

        binding.changePassword.setOnClickListener {
            changePasswordUi()
        }
        binding.changePaymentOption.setOnClickListener {
            startFragment(RemittanceFragment.newInstance(),true, RemittanceFragment.toString())
        }
        binding.myAddress.setOnClickListener {
            startFragment(ShippingAddressFragment.newInstance(),true, ShippingAddressFragment.toString())
        }

        return binding.root
    }

    fun changePasswordUi(){
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
                        settingViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ResponseSealed.Success->{
                        settingViewModel.methodRepo.hideLoadingDialog()
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
                        settingViewModel.methodRepo.hideLoadingDialog()
                        showCustomAlert(
                            it.message!!.message,
                            binding.root
                        )
                    }
                    is ResponseSealed.Empty -> {
                        settingViewModel.methodRepo.hideLoadingDialog()
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {

            }
    }
}