package com.octal.actorpay.ui.myOrderList

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.MainActivity
import com.octal.actorpay.databinding.RowCartItemBinding
import com.octal.actorpay.databinding.RowListOrderBinding
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderData
import com.octal.actorpay.ui.myOrderList.placeorder.PlaceOrderDialog

class OrderListAdapter(val orderList:MutableList<OrderData>,val fragmentManager:FragmentManager):RecyclerView.Adapter<OrderListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowListOrderBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(orderList[position])
    }

    override fun getItemCount(): Int {
       return orderList.size
    }

    inner class MyViewHolder(val binding: RowListOrderBinding):RecyclerView.ViewHolder(binding.root) {
            fun bindView(item:OrderData){
                binding.orderItem=item

                binding.recyclerViewOrder.layoutManager=LinearLayoutManager(binding.root.context)
                binding.recyclerViewOrder.adapter=OrderSingleAdapter(item.orderItemDtos)

                binding.root.setOnClickListener {
                    PlaceOrderDialog(binding.root.context,false,item){

                    }.show(fragmentManager,"Place")
                }



            }
    }

}