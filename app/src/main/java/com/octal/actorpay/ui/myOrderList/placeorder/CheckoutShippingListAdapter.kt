package com.octal.actorpay.ui.myOrderList.placeorder

import android.R.attr
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.PromoListItemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.promocodes.PromoItem
import android.R.attr.label

import android.content.ClipData
import android.content.ClipboardManager
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import com.octal.actorpay.R
import com.octal.actorpay.databinding.RowCheckoutAddressBinding
import com.octal.actorpay.databinding.ShippingListItemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem


class CheckoutShippingListAdapter(
    val context:Context,
    val list: MutableList<ShippingAddressItem>,
    val onClick:(position:Int,action:String)->Unit
) : RecyclerView.Adapter<CheckoutShippingListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowCheckoutAddressBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(list[position])

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

        fun bindView(item: ShippingAddressItem) {
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
            binding.root.setOnClickListener {
                onClick(adapterPosition,"Root")
            }
            binding.addressDelete.setOnClickListener {
                onClick(adapterPosition,"Delete")
            }
            binding.addressEdit.setOnClickListener {
                onClick(adapterPosition,"Edit")
            }


        }
    }

}
