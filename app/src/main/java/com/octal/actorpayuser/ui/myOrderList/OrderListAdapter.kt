package com.octal.actorpayuser.ui.myOrderList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowOrderListItemBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_CANCELLED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_COMPLETE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_DELIVERED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_DISPATCHED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_FAILED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_PARTIALLY_CANCELLED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_PARTIALLY_RETURNED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_PARTIALLY_RETURNING
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_PENDING
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_READY
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_RETURNED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_RETURNING
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_SUCCESS
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderData
import java.lang.Exception
import java.text.DecimalFormat

class OrderListAdapter(val methodsRepo: MethodsRepo, private val orderList:MutableList<OrderData>, val onClick:(position: Int, action: Clicks)->Unit):RecyclerView.Adapter<OrderListAdapter.MyViewHolder>() {

    var decimalFormat: DecimalFormat = DecimalFormat("0.00")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowOrderListItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(orderList[position],position)
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
            fun bindView(item:OrderData,position: Int){
                binding.orderItem=item
                binding.methodRepo=methodsRepo

                binding.orderStatus.text=item.orderStatus.replace("_"," ")
                binding.actualPriceText.text="Price: ₹"+decimalFormat.format(item.totalPrice)


                when {
                    item.orderItemDtos.size>3 -> {
                        binding.productImage1.visibility=View.VISIBLE
                        binding.productImage2.visibility=View.VISIBLE
                        binding.productImage3.visibility=View.VISIBLE
                        binding.productImage4.visibility=View.VISIBLE

                    }
                    item.orderItemDtos.size>2 -> {
                        binding.productImage1.visibility=View.VISIBLE
                        binding.productImage2.visibility=View.VISIBLE
                        binding.productImage3.visibility=View.VISIBLE
                    }
                    item.orderItemDtos.size>1 -> {
                        binding.productImage1.visibility=View.VISIBLE
                        binding.productImage2.visibility=View.VISIBLE
                    }
                    item.orderItemDtos.size>0 -> {
                        binding.productImage1.visibility=View.VISIBLE
                    }
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


                binding.root.setOnClickListener {
                    onClick(position,Clicks.Details)
                }

                if(item.orderStatus == STATUS_SUCCESS || item.orderStatus == STATUS_READY || item.orderStatus == STATUS_COMPLETE || item.orderStatus == STATUS_DISPATCHED || item.orderStatus == STATUS_DELIVERED)
                {
                    binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.green_color))
                    binding.orderStatus.setBackgroundResource(R.drawable.my_oder_status_bg)
                }
                else if(item.orderStatus == STATUS_CANCELLED || item.orderStatus == STATUS_PARTIALLY_CANCELLED || item.orderStatus == STATUS_FAILED){
                    binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.red))
                    binding.orderStatus.setBackgroundResource(R.drawable.my_oder_status_bg_red)
                }
                else if(item.orderStatus == STATUS_PENDING || item.orderStatus == STATUS_RETURNED || item.orderStatus == STATUS_RETURNING || item.orderStatus == STATUS_PARTIALLY_RETURNING || item.orderStatus == STATUS_PARTIALLY_RETURNED){
                    binding.orderStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.primary))
                    binding.orderStatus.setBackgroundResource(R.drawable.orderstatus_bg)
                }



            }
    }

}