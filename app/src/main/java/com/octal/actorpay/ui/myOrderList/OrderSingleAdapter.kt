package com.octal.actorpay.ui.myOrderList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpay.R
import com.octal.actorpay.databinding.RowCartItemBinding
import com.octal.actorpay.databinding.RowListOrderBinding
import com.octal.actorpay.databinding.RowOrderItemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderData
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderItemDtos

class OrderSingleAdapter(val itemList:MutableList<OrderItemDtos>):RecyclerView.Adapter<OrderSingleAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowOrderItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(itemList[position])
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    inner class MyViewHolder(val binding: RowOrderItemBinding):RecyclerView.ViewHolder(binding.root) {
            fun bindView(item:OrderItemDtos){
                binding.orderItem=item
                Glide.with(binding.root)
                    .load(item.image)
                    .error(R.drawable.logo)
                    .into(binding.productImage)


            }
    }

}