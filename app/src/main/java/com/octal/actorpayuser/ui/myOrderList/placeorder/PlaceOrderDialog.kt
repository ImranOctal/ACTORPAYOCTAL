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
    val onDone: (action:String,orderNo:String) -> Unit
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

        if(orderData.paymentMethod !=null)
            binding.orderPayment.text=orderData.paymentMethod
        else{
            binding.orderPayment.visibility=View.GONE
            binding.orderPaymentText.visibility=View.GONE
        }

        binding.orderNumber.text=orderData.orderNo
        binding.orderAmount.text=getString(R.string.rs).plus(orderData.totalPrice)
        binding.bussinessName.text="Business Name: "+orderData.merchantDTO.businessName
        binding.licenceNo.text="Licence No: "+orderData.merchantDTO.licenceNumber
        binding.email.text="Email: "+orderData.merchantDTO.email
        binding.contactNo.text="Contact No: "+orderData.merchantDTO.extensionNumber+""+orderData.merchantDTO.contactNumber
        binding.deliveryAddressAddress1.text=orderData.shippingAddressDTO!!.addressLine1
//        binding.deliveryAddressAddress2.text=orderData.shippingAddressDTO.addressLine2
//        binding.deliveryAddressCity.text=orderData.shippingAddressDTO.city+", "+orderData.shippingAddressDTO!!.state

        var addressLine2=""
        if(orderData.shippingAddressDTO.addressLine2==null || orderData.shippingAddressDTO.addressLine2.equals(""))
        {
            addressLine2=orderData.shippingAddressDTO.city+", "+orderData.shippingAddressDTO.state+", "+orderData.shippingAddressDTO.country
        }
        else{
            addressLine2=orderData.shippingAddressDTO.addressLine2!!+", "+orderData.shippingAddressDTO.city+", "+orderData.shippingAddressDTO.state+", "+orderData.shippingAddressDTO.country
        }
        binding.deliveryAddressAddress2.text=addressLine2

        binding.orderStatus.text=orderData.orderStatus.replace("_"," ")

        binding.orderDateText.text =
            "Order Date: " + methodsRepo.getFormattedOrderDate(orderData!!.createdAt)

        if (orderData.shippingAddressDTO == null) {
            binding.deliveryAddressAddress1.visibility = View.GONE
            binding.deliveryAddressAddress2.visibility = View.GONE
            binding.deliveryAddressCity.visibility = View.GONE
        }


        binding.order.setOnClickListener {
            dismiss()
            onDone("order",orderData.orderNo)
        }

        binding.shopping.setOnClickListener {
            dismiss()
            onDone("shopping","")
        }


        return dialog

    }
}