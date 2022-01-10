package com.octal.actorpay.ui.myOrderList.placeorder

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.databinding.PlaceOrderDialogBinding
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderData
import androidx.fragment.app.DialogFragment
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.utils.CommonDialogsUtils


//class PlaceOrderDialog(context: Context,val orderData: OrderData,val onDone:()->Unit):Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
class PlaceOrderDialog(val mContext: Activity,val methodsRepo: MethodsRepo,val isFromPlaceOrder:Boolean=true,val orderData: OrderData,val onDone:(String)->Unit):DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog=Dialog(mContext,R.style.MainDialog)

        val binding = DataBindingUtil.inflate<PlaceOrderDialogBinding>(layoutInflater,
            R.layout.place_order_dialog,
            null,
            false
        )
        dialog.setContentView(binding.root)
        setCancelable(false)

        if(isFromPlaceOrder.not()){
            binding.back.visibility= View.VISIBLE
            binding.done.visibility=View.GONE
            binding.successIcon.visibility=View.GONE
            binding.thankyouText.visibility=View.GONE
            binding.thankyouDescText.visibility=View.GONE
            setCancelable(true)
            if(orderData.orderStatus.equals("SUCCESS"))
            binding.cancel.visibility=View.VISIBLE
        }

        binding.back.setOnClickListener {
            dismiss()
        }

        binding.cancel.setOnClickListener {
            CommonDialogsUtils.showCommonDialog(mContext,methodsRepo,"Cancel Order","Are you sure?",true,true,true,false,object :CommonDialogsUtils.DialogClick{
                override fun onClick() {
                    dismiss()
                    onDone("cancel")
                }

                override fun onCancel() {

                }
            })


        }

        binding.orderRecyclerView.layoutManager=LinearLayoutManager(context)
        binding.orderRecyclerView.adapter= PlaceOrderAdapter(orderData.orderItemDtos)
        binding.orderData=orderData
        if(orderData.shippingAddressDTO==null)
        {
            binding.deliveryAddressAddress1.visibility=View.GONE
            binding.deliveryAddressAddress2.visibility=View.GONE
            binding.deliveryAddressCity.visibility=View.GONE
        }
        else if(orderData.shippingAddressDTO.addressLine2==null || orderData.shippingAddressDTO.addressLine2.equals("")){
            binding.deliveryAddressAddress2.visibility=View.GONE
        }
        binding.done.setOnClickListener {
            dismiss()
            onDone("done")
        }


        return dialog

    }
}