package com.octal.actorpayuser.ui.misc

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.octal.actorpayuser.R
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentMiscBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.ui.content.ContentActivity
import com.octal.actorpayuser.ui.content.ContentViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MiscFragment : BaseFragment() {
    private val miscViewModel: MiscViewModel by inject()
    private lateinit var binding:FragmentMiscBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_misc, container, false)
        init()
        apiResponse()


        return binding.root
    }

    fun init() {
        binding.apply {
            aboutUsText.setOnClickListener {
                ContentViewModel.type=1
                startActivity(Intent(requireContext(), ContentActivity::class.java))
            }
            tcText.setOnClickListener {
                ContentViewModel.type=3
                startActivity(Intent(requireContext(), ContentActivity::class.java))
            }
            privacyText.setOnClickListener {
                ContentViewModel.type=2
                startActivity(Intent(requireContext(), ContentActivity::class.java))
            }
            faqText.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(R.id.faqFragment)
            }
            myAddress.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(R.id.shippingAddressFragment)

            }

        }
    }



    private fun apiResponse(){
        lifecycleScope.launch {
            miscViewModel.miscResponseLive.collect {
                when(it){
                    is ResponseSealed.loading->{
                        showLoading()
                    }
                    is ResponseSealed.Success->{
                        hideLoading()
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
                    is ResponseSealed.ErrorOnResponse->{
                        hideLoading()
                        (requireActivity() as BaseActivity).showCustomAlert(
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