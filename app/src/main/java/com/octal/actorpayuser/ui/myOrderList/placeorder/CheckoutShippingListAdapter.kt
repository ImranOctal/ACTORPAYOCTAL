package com.octal.actorpayuser.ui.myOrderList.placeorder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowCheckoutAddressBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressItem


class CheckoutShippingListAdapter(
    val context:Context,
    val list: MutableList<ShippingAddressItem>,
    val onClick:(position:Int,action:Clicks)->Unit
) : RecyclerView.Adapter<CheckoutShippingListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowCheckoutAddressBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(list[position],position)

    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return  position
    }

    inner class MyViewHolder(val binding: RowCheckoutAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: ShippingAddressItem,position: Int) {
            binding.shippingItem = item

            if(item.isSelect!=null && item.isSelect!!){
                binding.addressCheck.visibility= View.VISIBLE
                binding.addressEdit.visibility= View.VISIBLE
                binding.addressDelete.visibility= View.VISIBLE
                binding.addressLayout.setBackgroundResource(R.drawable.orderstatus_bg)
            }
            else {
                binding.addressCheck.visibility= View.GONE
                binding.addressEdit.visibility= View.GONE
                binding.addressDelete.visibility= View.GONE
                binding.addressLayout.setBackgroundResource(R.drawable.btn_outline_gray)
            }

            if(item.primary)
                binding.addressDelete.visibility= View.GONE

            binding.root.setOnClickListener {
                onClick(position,Clicks.Root)
            }
            binding.addressDelete.setOnClickListener {
                onClick(position,Clicks.Delete)
            }
            binding.addressEdit.setOnClickListener {
                onClick(position,Clicks.Edit)
            }


        }
    }

}
