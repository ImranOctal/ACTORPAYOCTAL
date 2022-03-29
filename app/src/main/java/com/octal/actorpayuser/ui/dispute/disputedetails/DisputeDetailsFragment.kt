package com.octal.actorpayuser.ui.dispute.disputedetails

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentDisputeDetailsBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.*
import com.octal.actorpayuser.ui.dispute.DisputeViewModel
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class DisputeDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentDisputeDetailsBinding
    private val disputeDetailsViewModel: DisputeDetailsViewModel by inject()

    private var disputeID: String =""
    private var disputeCode: String =""
    private var disputeData: DisputeData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            disputeID = it.getString("disputeId")!!
            disputeCode = it.getString("disputeCode")!!
        }
        if(disputeID != ""){
            disputeDetailsViewModel.disputeListParams.disputeCode=disputeCode
            disputeDetailsViewModel.getAllDisputes()
        }
        else{
            showCustomToast("Something went wrong")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dispute_details, container, false)

        if(disputeData!=null)
            updateUI(disputeData!!)

        apiResponse()


        binding.sendMessageIcon.setOnClickListener {
                sendMessage()
        }


        binding.messageRefresh.setDistanceToTriggerSync(50)
        binding.messageRefresh.setOnRefreshListener {
            disputeDetailsViewModel.getAllDisputes()
            binding.messageRefresh.isRefreshing=false
        }



        return binding.root
    }

    fun sendMessage(){
        val message=binding.sendMessageEdt.text.toString().trim()
        if(message == ""){
            return
        }
        binding.sendMessageEdt.setText("")
        disputeDetailsViewModel.sendDisputeMessage(SendMessageParams(disputeID,message))
    }

    fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            disputeDetailsViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
//                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is DisputeSingleResponse -> {
                                updateUI(event.response.data)
                            }
                            is DisputeListResponse->{
                                updateUI(event.response.data.items[0])
                            }
                            is SuccessResponse -> {
//                                showCustomToast(event.response.message)
                                disputeDetailsViewModel.getAllDisputes()
//                                requireActivity().onBackPressed()
                            }
                        }
                        disputeDetailsViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        disputeDetailsViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(disputeDetailsViewModel.methodRepo)
                        }
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    fun updateUI(disputeData: DisputeData){
        this.disputeData=disputeData
        binding.disputedata=disputeData
        binding.createdDate.text=disputeDetailsViewModel.methodRepo.getFormattedOrderDate(disputeData.createdAt)
            if(disputeData.disputeMessages!=null)
            setAdapter(disputeData.disputeMessages)

        binding.order.setPaintFlags(binding.order.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.order.setOnClickListener {
            val bundle =
                bundleOf("orderNo" to disputeData.orderNo)
            Navigation.findNavController(requireView())
                .navigate(R.id.orderDetailsFragment, bundle, null)
        }
    }

    fun setAdapter(disputeMessages:MutableList<DisputeMessage>){
        val adapter=DisputeMessageAdapter(requireContext(),disputeDetailsViewModel.methodRepo,disputeMessages)
        val layoutManager=LinearLayoutManager(requireContext())
        layoutManager.reverseLayout=true
        binding.rvMessages.layoutManager=layoutManager
        binding.rvMessages.adapter=adapter
        if(disputeMessages.size>0)
        binding.rvMessages.scrollToPosition(0)

    }


}