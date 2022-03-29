package com.octal.actorpayuser.ui.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentFAQBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.misc.FAQResponse
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class FAQFragment : BaseFragment() {


    private val miscViewModel: MiscViewModel by inject()
    private lateinit var binding: FragmentFAQBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_f_a_q, container, false)

        miscViewModel.getFAQ()

        apiResponse()

        return binding.root
    }

    private fun initExpandableList(){


        val adapter=CustomFAQAdapter(miscViewModel.faqList)
        binding.rvFaq.layoutManager=LinearLayoutManager(requireContext())
        binding.rvFaq.adapter=adapter


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

                        when (it.response) {
                            is FAQResponse -> {
                                initExpandableList()
                            }
                            is SuccessResponse -> {
                                CommonDialogsUtils.showCommonDialog(requireActivity(),miscViewModel.methodRepo,"Success",it.response.message)
                            }
                            else -> {
                                showCustomAlert(
                                    getString(R.string.please_try_after_sometime),
                                    binding.root
                                )
                            }
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