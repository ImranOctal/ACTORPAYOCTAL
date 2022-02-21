package com.octal.actorpayuser.ui.myOrderList.placeorder

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.PlaceOrderDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderData


class PlaceOrderDialog(
    private val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val orderData: OrderData,
    val onDone: (String) -> Unit
) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(mContext, R.style.MainDialog)

        val binding = DataBindingUtil.inflate<PlaceOrderDialogBinding>(
            layoutInflater,
            R.layout.place_order_dialog,
            null,
            false
        )
        dialog.setContentView(binding.root)
        isCancelable = false

        binding.back.setOnClickListener {
            dismiss()
        }

        binding.orderRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.orderRecyclerView.adapter = PlaceOrderAdapter(mContext,orderData.orderItemDtos){
                pos, action ->
        }

        binding.orderNumber.text=orderData.orderNo
        binding.orderAmount.text=getString(R.string.rs).plus(orderData.totalPrice)
        binding.bussinessName.text="Business Name: "+orderData.merchantDTO.businessName
        binding.licenceNo.text="Licence No: "+orderData.merchantDTO.licenceNumber
        binding.email.text="Email: "+orderData.merchantDTO.email
        binding.contactNo.text="Contact No: "+orderData.merchantDTO.extensionNumber+""+orderData.merchantDTO.contactNumber
        binding.deliveryAddressAddress1.text=orderData.shippingAddressDTO!!.addressLine1
        binding.deliveryAddressAddress2.text=orderData.shippingAddressDTO.addressLine2
        binding.deliveryAddressCity.text=orderData.shippingAddressDTO.city+", "+orderData.shippingAddressDTO!!.state

        binding.orderStatus.text=orderData.orderStatus.replace("_"," ")

        binding.orderDateText.text =
            "Order Date: " + methodsRepo.getFormattedOrderDate(orderData!!.createdAt)

        if (orderData.shippingAddressDTO == null) {
            binding.deliveryAddressAddress1.visibility = View.GONE
            binding.deliveryAddressAddress2.visibility = View.GONE
            binding.deliveryAddressCity.visibility = View.GONE
        } else if (orderData.shippingAddressDTO.addressLine2 == null || orderData.shippingAddressDTO?.addressLine2.equals("")) {
            binding.deliveryAddressAddress2.visibility = View.GONE
        }


        binding.order.setOnClickListener {
            dismiss()
            onDone("order")
        }

        binding.shopping.setOnClickListener {
            dismiss()
            onDone("shopping")
        }


        return dialog

    }
}