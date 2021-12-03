package com.octal.actorpay.ui.misc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentFAQBinding
import com.octal.actorpay.databinding.FragmentMiscBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.misc.FAQResponse
import com.octal.actorpay.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 * Use the [FAQFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FAQFragment : BaseFragment() {


    private val miscViewModel: MiscViewModel by inject()
    private lateinit var binding: FragmentFAQBinding
    override fun WorkStation() {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_f_a_q, container, false)

        (requireActivity() as MainActivity).title="FAQ"

        lifecycleScope.launchWhenCreated {
        miscViewModel.getFAQ()
        }

        ApiResponse()

        return binding.root
    }

    fun initExpandableList(){
        val adapter:ExpandableListAdapter = CustomExpandableListAdapter(requireContext(), miscViewModel.faqList)
        binding.
            expendableList.setAdapter(adapter)
            binding.expendableList.setOnGroupExpandListener (object :
                ExpandableListView.OnGroupExpandListener {
                var previousGroup = -1
                var flag = false
                override fun onGroupExpand(groupPosition: Int) {
                    if (groupPosition != previousGroup && flag) {
                        binding.expendableList.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;

                    flag = true;
                }
            })
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