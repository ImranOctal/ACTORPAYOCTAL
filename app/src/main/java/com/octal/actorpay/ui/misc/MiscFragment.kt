package com.octal.actorpay.ui.misc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.R
import com.octal.actorpay.Utils.CommonDialogsUtils
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentMiscBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpay.ui.dashboard.bottomnavfragments.viewmodels.ProfileViewModel
import com.octal.actorpay.ui.remittance.RemittanceFragment
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MiscFragment : BaseFragment() {
    private val miscViewModel: MiscViewModel by inject()
    private lateinit var binding:FragmentMiscBinding


    override fun WorkStation() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        private var instance: MiscFragment? = null
        @JvmStatic
        fun newInstance(): MiscFragment? {

            if (instance == null) {
                instance = MiscFragment()
            }
            return instance
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_misc, container, false)

        init()
        ApiResponse()


        return binding.root
    }

    fun init() {
        binding.apply {
            miscChangePassword.setOnClickListener {
                changePasswordUi()
            }
        }
    }

    fun changePasswordUi(){
        ChangePasswordDialog().show(requireActivity(),miscViewModel.methodRepo){
            oldPassword, newPassword ->
            miscViewModel.changePassword(oldPassword,newPassword    )
        }
    }

    private fun ApiResponse(){
        lifecycleScope.launch {
            miscViewModel.miscResponseLive.collect {
                when(it){
                    is MiscViewModel.ResponseMiscSealed.loading->{
                        miscViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is MiscViewModel.ResponseMiscSealed.Success->{
                        miscViewModel.methodRepo.hideLoadingDialog()
                        if(it.response is SuccessResponse){
                            CommonDialogsUtils.showCommonDialog(requireActivity(),miscViewModel.methodRepo,"Success",it.response.message)
                        }

                        else {
                            showCustomAlert(
                                getString(R.string.please_try_after_sometime),
                                binding.root
                            )
                        }
                    }
                    is MiscViewModel.ResponseMiscSealed.ErrorOnResponse->{
                        miscViewModel.methodRepo.hideLoadingDialog()
                        (requireActivity() as BaseActivity).showCustomAlert(
                            it.failResponse!!.message,
                            binding.root
                        )
                    }
                    is MiscViewModel.ResponseMiscSealed.Empty -> {
                        miscViewModel.methodRepo.hideLoadingDialog()
                    }
                }
            }
        }
    }

}