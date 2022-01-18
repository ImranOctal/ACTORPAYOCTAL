package com.octal.actorpay.ui.myOrderList.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentOrderDetailsBinding
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_SUCCESS
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_READY
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_CANCELLED
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_PARTIALLY_CANCELLED
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_DISPATCHED
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_DELIVERED
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_RETURNING
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_PARTIALLY_RETURNING
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_RETURNED
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_PARTIALLY_RETURNED
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_PENDING
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.STATUS_FAILED
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderData
import com.octal.actorpay.ui.myOrderList.placeorder.PlaceOrderAdapter
import com.octal.actorpay.utils.CommonDialogsUtils
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.io.File

private const val ARG_PARAM1 = "param1"


class OrderDetailsFragment : BaseFragment() {
    private var orderData: OrderData? = null
    private val orderDetailsViewModel: OrderDetailsViewModel by inject()
    private lateinit var binding: FragmentOrderDetailsBinding
    override fun WorkStation() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderData = it.getSerializable(ARG_PARAM1) as OrderData

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false)

        setTitle("Order Summary")
        showHideBottomNav(false)
        showHideCartIcon(false)
        showHideFilterIcon(false)

        if(orderData?.orderStatus.equals(STATUS_SUCCESS) || orderData?.orderStatus.equals(STATUS_READY) || orderData?.orderStatus.equals(
                STATUS_PARTIALLY_CANCELLED)){
            binding.cancel.text=getString(R.string.cancel)
            binding.cancel.visibility=View.VISIBLE
        }
        else if(orderData?.orderStatus.equals(STATUS_DELIVERED) || orderData?.orderStatus.equals(STATUS_DISPATCHED)){
            binding.cancel.text=getString(R.string.return_order)
            binding.cancel.visibility=View.VISIBLE
        }

        binding.cancel.setOnClickListener {

            CommonDialogsUtils.showCommonDialog(requireActivity(),orderDetailsViewModel.methodRepo,"Cancel Order","Are you sure?", isCancelAvailable = true, callback = object :CommonDialogsUtils.DialogClick{
                override fun onClick() {
                    cancelReturnOrder()
                }
                override fun onCancel() {

                }
            })
        }

        binding.orderRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.orderRecyclerView.adapter= PlaceOrderAdapter(orderData!!.orderItemDtos)
        binding.orderData=orderData
        if(orderData?.shippingAddressDTO==null)
        {
            binding.deliveryAddressAddress1.visibility=View.GONE
            binding.deliveryAddressAddress2.visibility=View.GONE
            binding.deliveryAddressCity.visibility=View.GONE
        }
        else if(orderData?.shippingAddressDTO?.addressLine2==null || orderData?.shippingAddressDTO?.addressLine2.equals("")){
            binding.deliveryAddressAddress2.visibility=View.GONE
        }

        return binding.root
    }

    fun cancelReturnOrder(){
        var status=""
        val orderIdArray=JSONArray()


        if(orderData?.orderStatus.equals(STATUS_SUCCESS) || orderData?.orderStatus.equals(STATUS_READY))
        {
            status="CANCELLED"
            orderData?.orderItemDtos!!.forEach {
                orderIdArray.put(it.orderItemId)
            }
        }
        else if(orderData?.orderStatus.equals(
                STATUS_PARTIALLY_CANCELLED))
        {
            status="CANCELLED"
            orderData?.orderItemDtos?.forEach {
                if(it.orderItemStatus.equals("SUCCESS") || it.orderItemStatus.equals("READY"))
                    orderIdArray.put(it.orderItemId)
            }
        }

        if(status.isNotEmpty()) {
            var prodImage: File? = null
            val cancelOrderJson = JSONObject()

            CancelOrderDialog(requireActivity(),orderDetailsViewModel.methodRepo,false){
                    reason, file ->
                cancelOrderJson.put("cancellationRequest",status)
                cancelOrderJson.put("cancelReason",reason)
                cancelOrderJson.put("orderItemIds",orderIdArray)
                showCustomToast(reason)
            }.show(childFragmentManager,"Place")
//                orderDetailsViewModel.changeOrderItemsStatus(orderData!!.orderNo,cancelOrderJson.toString(),prodImage)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(orderData: OrderData) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, orderData)

                }
            }
    }
}