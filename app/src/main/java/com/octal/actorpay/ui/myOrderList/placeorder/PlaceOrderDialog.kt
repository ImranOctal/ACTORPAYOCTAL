package com.octal.actorpay.ui.myOrderList.placeorder

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


//class PlaceOrderDialog(context: Context,val orderData: OrderData,val onDone:()->Unit):Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
class PlaceOrderDialog(val mContext: Context,val isFromPlaceOrder:Boolean=true,val orderData: OrderData,val onDone:()->Unit):DialogFragment() {


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
        }

        binding.back.setOnClickListener {
            dismiss()
        }

        binding.orderRecyclerView.layoutManager=LinearLayoutManager(context)
        binding.orderRecyclerView.adapter= PlaceOrderAdapter(orderData.orderItemDtos)
        binding.orderData=orderData
        binding.done.setOnClickListener {
            dismiss()
            onDone()
        }


        return dialog

    }
}