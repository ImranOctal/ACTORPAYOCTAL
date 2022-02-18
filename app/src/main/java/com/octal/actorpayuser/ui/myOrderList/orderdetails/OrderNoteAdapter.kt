package com.octal.actorpayuser.ui.myOrderList.orderdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowOrderNoteBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderNote

class OrderNoteAdapter(val methodsRepo: MethodsRepo,val orderNotes:MutableList<OrderNote>):RecyclerView.Adapter<OrderNoteAdapter.MyViewHolder>() {

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


            binding.orderNoteDate.text=methodsRepo.getFormattedOrderDate(orderNote.createdAt)

            if(orderNote.userType == "merchant") {
                binding.orderNoteUser.text="Merchant :"
                binding.orderNoteUser.visibility = View.VISIBLE
            }
            else if(orderNote.userType == "customer") {
                binding.orderNoteUser.text="Me :"
                binding.orderNoteUser.visibility = View.VISIBLE
            }

             if(orderNote.orderStatus !=null && orderNote.orderStatus == AppConstance.STATUS_SUCCESS) {
                binding.orderNoteDesc.visibility = View.GONE
                binding.orderNoteUser.visibility=View.GONE
            }




            if(orderNote.orderStatus == null)
                binding.orderNoteStatus.visibility=View.GONE

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