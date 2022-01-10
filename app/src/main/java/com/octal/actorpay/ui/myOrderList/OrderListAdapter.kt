package com.octal.actorpay.ui.myOrderList

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.CameraPosition
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.databinding.RowCartItemBinding
import com.octal.actorpay.databinding.RowListOrderBinding
import com.octal.actorpay.databinding.RowOrderListItemBinding
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderData
import com.octal.actorpay.ui.myOrderList.placeorder.PlaceOrderDialog
import java.lang.Exception

class OrderListAdapter(val mContext: Activity,val methodsRepo: MethodsRepo, val orderList:MutableList<OrderData>, val fragmentManager:FragmentManager, val onClick:(position: Int, action:String)->Unit):RecyclerView.Adapter<OrderListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowOrderListItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(orderList[position])
    }

    override fun getItemCount(): Int {
       return orderList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(val binding: RowOrderListItemBinding):RecyclerView.ViewHolder(binding.root) {
            fun bindView(item:OrderData){
                binding.orderItem=item

                if(item.orderItemDtos.size>3){
                    binding.productImage1.visibility=View.VISIBLE
                    binding.productImage2.visibility=View.VISIBLE
                    binding.productImage3.visibility=View.VISIBLE
                    binding.productImage4.visibility=View.VISIBLE

                }
                else if(item.orderItemDtos.size>2){
                    binding.productImage1.visibility=View.VISIBLE
                    binding.productImage2.visibility=View.VISIBLE
                    binding.productImage3.visibility=View.VISIBLE
                }
                else if(item.orderItemDtos.size>1){
                    binding.productImage1.visibility=View.VISIBLE
                    binding.productImage2.visibility=View.VISIBLE
                }
                else if(item.orderItemDtos.size>0){
                    binding.productImage1.visibility=View.VISIBLE
                }
                try {
                    Glide.with(binding.root)
                        .load(item.orderItemDtos[0].image)
                        .error(R.drawable.logo)
                        .into(binding.productImage1)
                    Glide.with(binding.root)
                        .load(item.orderItemDtos[1].image)
                        .error(R.drawable.logo)
                        .into(binding.productImage2)
                    Glide.with(binding.root)
                        .load(item.orderItemDtos[2].image)
                        .error(R.drawable.logo)
                        .into(binding.productImage3)
                    Glide.with(binding.root)
                        .load(item.orderItemDtos[3].image)
                        .error(R.drawable.logo)
                        .into(binding.productImage4)
                }
                catch (e:Exception){

                }
                Glide.with(binding.root)
                    .load(item.orderItemDtos[0].image)
                    .error(R.drawable.logo)
                    .into(binding.productImage1)

               /* binding.recyclerViewOrder.layoutManager=LinearLayoutManager(binding.root.context)
                binding.recyclerViewOrder.adapter=OrderSingleAdapter(item.orderItemDtos)
                    */
                binding.root.setOnClickListener {
                    PlaceOrderDialog(mContext,methodsRepo,false,item){
                            if(it.equals("cancel")){
                                onClick(adapterPosition,"cancel")
                            }
                    }.show(fragmentManager,"Place")
                }

                if(item.orderStatus.equals("SUCCESS") || item.orderStatus.equals("COMPLETED"))
                {
                    binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.green_color))
                    binding.orderStatus.setBackgroundResource(R.drawable.my_oder_status_bg)
                }
                else if(item.orderStatus.equals("CANCELLED") || item.orderStatus.equals("FAILED") || item.orderStatus.equals("CANCEL")){
                    binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.red))
                    binding.orderStatus.setBackgroundResource(R.drawable.my_oder_status_bg_red)
                }
                else if(item.orderStatus.equals("PENDING") || item.orderStatus.equals("RETURNED") ){
                    binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.primary))
                    binding.orderStatus.setBackgroundResource(R.drawable.orderstatus_bg)
                }



            }
    }

}