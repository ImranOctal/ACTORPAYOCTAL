package com.octal.actorpayuser.ui.myOrderList.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.CancelBottomsheetBinding
import com.octal.actorpayuser.databinding.FragmentOrderDetailsBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_CANCELLED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_PARTIALLY_CANCELLED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_READY
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_RETURNING
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_SUCCESS
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.DisputeData
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.RaiseDisputeResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderNoteResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.SingleOrderResponse
import com.octal.actorpayuser.ui.dispute.RaiseDisputeDialog
import com.octal.actorpayuser.ui.dispute.RaiseDisputeSuccessDialog
import com.octal.actorpayuser.ui.myOrderList.placeorder.PlaceOrderAdapter
import kotlinx.coroutines.flow.collect
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.android.inject


class OrderDetailsFragment : BaseFragment() {
    private var orderNo: String =""
    private val orderDetailsViewModel: OrderDetailsViewModel by inject()
    private lateinit var binding: FragmentOrderDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiResponse()
        arguments?.let {
            orderNo = it.getString("orderNo")!!
        }
        if(orderNo != ""){
            orderDetailsViewModel.getOrder(orderNo)
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false)
        binding.pullToRefresh.setOnRefreshListener {
            orderDetailsViewModel.getOrder(orderNo)
            binding.pullToRefresh.isRefreshing=false
        }

        binding.btnNote.setOnClickListener {
            addNote()
        }

        return binding.root
    }

    fun updateUI(){
        binding.orderStatus.visibility=View.VISIBLE
        binding.orderNumber.text=orderDetailsViewModel.orderData!!.orderNo
        binding.orderAmount.text=getString(R.string.rs).plus(orderDetailsViewModel.orderData!!.totalPrice)
        binding.bussinessName.text="Business Name: "+orderDetailsViewModel.orderData!!.merchantDTO.businessName
        binding.licenceNo.text="Licence No: "+orderDetailsViewModel.orderData!!.merchantDTO.licenceNumber
        binding.email.text="Email: "+orderDetailsViewModel.orderData!!.merchantDTO.email
        binding.contactNo.text="Contact No: "+orderDetailsViewModel.orderData!!.merchantDTO.extensionNumber+""+orderDetailsViewModel.orderData!!.merchantDTO.contactNumber
        binding.deliveryAddressAddress1.text=orderDetailsViewModel.orderData!!.shippingAddressDTO!!.addressLine1
        binding.deliveryAddressAddress2.text=orderDetailsViewModel.orderData!!.shippingAddressDTO!!.addressLine2
        binding.deliveryAddressCity.text=orderDetailsViewModel.orderData!!.shippingAddressDTO!!.city+", "+orderDetailsViewModel.orderData!!.shippingAddressDTO!!.state

        binding.orderStatus.text=orderDetailsViewModel.orderData!!.orderStatus.replace("_"," ")

        if(orderDetailsViewModel.orderData!!.orderStatus == STATUS_SUCCESS || orderDetailsViewModel.orderData!!.orderStatus == STATUS_READY || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_COMPLETE || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_DISPATCHED || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_DELIVERED)
        {
            binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.green_color))
            binding.orderStatus.setBackgroundResource(R.drawable.my_oder_status_bg)
        }
        else if(orderDetailsViewModel.orderData!!.orderStatus == STATUS_CANCELLED || orderDetailsViewModel.orderData!!.orderStatus == STATUS_PARTIALLY_CANCELLED || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_FAILED){
            binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.red))
            binding.orderStatus.setBackgroundResource(R.drawable.my_oder_status_bg_red)
        }
        else if(orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_PENDING || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_RETURNED || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_RETURNING || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_PARTIALLY_RETURNING || orderDetailsViewModel.orderData!!.orderStatus == AppConstance.STATUS_PARTIALLY_RETURNED){
            binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.primary))
            binding.orderStatus.setBackgroundResource(R.drawable.orderstatus_bg)
        }

        binding.orderRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.orderRecyclerView.adapter =
            PlaceOrderAdapter(requireContext(),orderDetailsViewModel.orderData!!.orderItemDtos, false) {
                    pos,status ->
                if(status == STATUS_CANCELLED || status== STATUS_RETURNING)
                cancelReturnOrder(status,pos)
                else if(status == AppConstance.STATUS_DISPUTE)
                    raiseDispute(pos)
            }

        binding.orderDateText.text =
            "Order Date: " + orderDetailsViewModel.methodRepo.getFormattedOrderDate(orderDetailsViewModel.orderData!!.createdAt)


        if (orderDetailsViewModel.orderData?.shippingAddressDTO == null) {
            binding.deliveryAddressAddress1.visibility = View.GONE
            binding.deliveryAddressAddress2.visibility = View.GONE
            binding.deliveryAddressCity.visibility = View.GONE
        } else if (orderDetailsViewModel.orderData?.shippingAddressDTO?.addressLine2 == null || orderDetailsViewModel.orderData?.shippingAddressDTO?.addressLine2.equals(
                ""
            )
        ) {
            binding.deliveryAddressAddress2.visibility = View.GONE
        }
        binding.orderNotesRecyclerView.layoutManager = LinearLayoutManager(context)
        val notesList=orderDetailsViewModel.orderData!!.orderNotesDtos.filter {
            it.orderNoteDescription!=null && it.orderNoteDescription != ""
        }.toMutableList()
        binding.orderNotesRecyclerView.adapter=OrderNoteAdapter(orderDetailsViewModel.methodRepo,notesList)
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            orderDetailsViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                       showLoading()
                    }
                    is ResponseSealed.Success -> {
                       hideLoading()
                        when (event.response) {
                            is SingleOrderResponse -> {
                               orderDetailsViewModel.orderData=event.response.data
                                updateUI()
                            }
                            is OrderNoteResponse ->{
                                showCustomToast("Add Order Note Successfully")
                                orderDetailsViewModel.getOrder(orderNo)
                            }
                            is SuccessResponse ->{
                                orderDetailsViewModel.getOrder(orderNo)
                            }
                            is RaiseDisputeResponse ->{
                                showCustomToast("success")
                                showRaisedDiputeDialog(event.response.data)
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        showCustomToast(event.message!!.message)

                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()
                    }
                }
            }

        }
    }

    fun showRaisedDiputeDialog(disputeData: DisputeData){
            RaiseDisputeSuccessDialog().showDialog(requireActivity(),orderDetailsViewModel.methodRepo,disputeData)
    }



    private fun addNote() {
        val binding: CancelBottomsheetBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.cancel_bottomsheet, null, false)
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        binding.btnSubmit.setOnClickListener {
            if (binding.etNote.text.isEmpty()) {
                showCustomToast(getString(R.string.add_note_description))
            } else {
                dialog.dismiss()
                orderDetailsViewModel.addNote(binding.etNote.text.toString().trim(), orderDetailsViewModel.orderData!!.orderNo)
            }
        }
        binding.btnCancnel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(binding.root)
        dialog.show()

    }

    fun cancelReturnOrder(status:String,pos:Int) {

        val orderIdArray = JSONArray()
        orderIdArray.put(orderDetailsViewModel.orderData!!.orderItemDtos[pos].orderItemId)

        if (status.isNotEmpty()) {

            val cancelOrderJson = JSONObject()

            CancelOrderDialog(
                requireActivity(),
                orderDetailsViewModel.methodRepo,
                status == STATUS_CANCELLED,
                orderDetailsViewModel.orderData,
                pos,
            ) { reason, file ->

                cancelOrderJson.put("cancellationRequest", status)
                cancelOrderJson.put("cancelReason", reason)
                cancelOrderJson.put("orderItemIds", orderIdArray)
                orderDetailsViewModel.changeOrderItemsStatus(orderDetailsViewModel.orderData!!.orderNo,cancelOrderJson.toString(),file)
//                showCustomToast(reason)
            }.show(childFragmentManager, "Place")
        }
    }

    fun raiseDispute(pos:Int){

            val raiseDiputeJson = JSONObject()

            RaiseDisputeDialog(
                requireActivity(),
                orderDetailsViewModel.methodRepo,
                orderDetailsViewModel.orderData,
                pos,
            ) { title, reason, file ->

                raiseDiputeJson.put("title", title)
                raiseDiputeJson.put("description", reason)
                raiseDiputeJson.put("orderItemId", orderDetailsViewModel.orderData!!.orderItemDtos[pos].orderItemId)
                orderDetailsViewModel.raiseDipute(raiseDiputeJson.toString(),file)
//                showCustomToast(reason)
            }.show(childFragmentManager, "Place")

    }

}