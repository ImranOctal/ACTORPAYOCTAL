package com.octal.actorpay.ui.myOrderList.placeorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpay.R
import com.octal.actorpay.databinding.RowOrderItemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderItemDtos

class PlaceOrderAdapter(private var orderList: ArrayList<OrderItemDtos>):RecyclerView.Adapter<PlaceOrderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowOrderItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class MyViewHolder(val binding:RowOrderItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bindView(item:OrderItemDtos)
        {
            binding.orderItem=item
            Glide.with(binding.root)
                .load(item.image)
                .error(R.drawable.logo)
                .into(binding.productImage)
        }
    }


}