package com.octal.actorpayuser.ui.dispute.disputedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentDisputeBinding
import com.octal.actorpayuser.databinding.FragmentDisputeDetailsBinding
import com.octal.actorpayuser.ui.dispute.DisputeViewModel
import org.koin.android.ext.android.inject

class DisputeDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentDisputeDetailsBinding
    private val disputeDetailsViewModel: DisputeDetailsViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dispute_details, container, false)

        binding.disputedata=DisputeDetailsViewModel.disputeData
        binding.createdDate.text=disputeDetailsViewModel.methodRepo.getFormattedOrderDate(DisputeDetailsViewModel.disputeData!!.createdAt)
        setAdapter()



        return binding.root
    }

    fun setAdapter(){
        val adapter=DisputeMessageAdapter(requireContext(),disputeDetailsViewModel.methodRepo,DisputeDetailsViewModel.disputeData!!.disputeMessages.asReversed())
        val layoutManager=LinearLayoutManager(requireContext())
        layoutManager.reverseLayout=true
        binding.rvMessages.layoutManager=layoutManager
        binding.rvMessages.adapter=adapter

    }


}