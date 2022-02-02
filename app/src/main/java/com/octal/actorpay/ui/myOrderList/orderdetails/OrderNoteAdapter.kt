package com.octal.actorpay.ui.myOrderList.orderdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.R
import com.octal.actorpay.databinding.RowOrderNoteBinding
import com.octal.actorpay.repositories.AppConstance.AppConstance
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderNote

class OrderNoteAdapter(val orderNotes:MutableList<OrderNote>):RecyclerView.Adapter<OrderNoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowOrderNoteBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(orderNotes[position])
    }

    override fun getItemCount(): Int {
        return orderNotes.size
    }

    inner class MyViewHolder(val binding:RowOrderNoteBinding):RecyclerView.ViewHolder(binding.root) {

        fun bindView(orderNote:OrderNote){
                binding.orderNote=orderNote
            if(orderNote.orderStatus == AppConstance.STATUS_SUCCESS)
                binding.orderNoteDesc.visibility= View.GONE

            if(orderNote.orderStatus == AppConstance.STATUS_SUCCESS || orderNote.orderStatus == AppConstance.STATUS_READY || orderNote.orderStatus == AppConstance.STATUS_COMPLETE || orderNote.orderStatus == AppConstance.STATUS_DISPATCHED || orderNote.orderStatus == AppConstance.STATUS_DELIVERED)
            {
                binding.orderNoteStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_color))
                binding.orderNoteView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.green_color))
            }
            else if(orderNote.orderStatus == AppConstance.STATUS_CANCELLED || orderNote.orderStatus == AppConstance.STATUS_PARTIALLY_CANCELLED || orderNote.orderStatus == AppConstance.STATUS_FAILED){
                binding.orderNoteStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                binding.orderNoteView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.red))
            }
            else if(orderNote.orderStatus == AppConstance.STATUS_PENDING || orderNote.orderStatus == AppConstance.STATUS_RETURNED || orderNote.orderStatus == AppConstance.STATUS_RETURNING || orderNote.orderStatus == AppConstance.STATUS_PARTIALLY_RETURNING || orderNote.orderStatus == AppConstance.STATUS_PARTIALLY_RETURNED){
                binding.orderNoteStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary))
                binding.orderNoteView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.primary))
            }
        }
    }

}