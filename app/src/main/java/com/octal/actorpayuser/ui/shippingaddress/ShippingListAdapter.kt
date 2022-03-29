package com.octal.actorpayuser.ui.shippingaddress

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.octal.actorpayuser.databinding.ShippingListItemBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressItem


class ShippingListAdapter(
    val context:Context,
    val list: MutableList<ShippingAddressItem>,
    val onClick:(position:Int,action:String)->Unit
) : RecyclerView.Adapter<ShippingListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShippingListItemBinding.inflate(inflater, parent, false)
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

    inner class MyViewHolder(val binding: ShippingListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: ShippingAddressItem,position: Int) {
            binding.shippingItem = item
            var addressLine2=""
            if(item.addressLine2==null || item.addressLine2.equals(""))
            {
               addressLine2=item.city+", "+", "+item.state+", "+item.country
            }
            else{
                addressLine2=item.addressLine2!!+", "+item.city+item.state+", "+", "+item.country
            }

            binding.addressLine2.text=addressLine2

            if(item.primary) {
                binding.delete.visibility = View.GONE
                binding.deleteIcon.visibility = View.GONE
                binding.primary.visibility = View.VISIBLE
            }
            else {
                binding.delete.visibility = View.VISIBLE
                binding.deleteIcon.visibility = View.VISIBLE
                binding.primary.visibility = View.GONE
            }


            binding.delete.setOnClickListener {
                onClick(position,"Delete")
            }
            binding.deleteIcon.setOnClickListener {
                onClick(position,"Delete")
            }
            binding.edit.setOnClickListener {
                onClick(position,"Edit")
            }
            binding.editIcon.setOnClickListener {
                onClick(position,"Edit")
            }

         /*   Glide.with(binding.root)
                .load(item.image)
                .error(R.drawable.logo)
                .into(binding.productImage)*/


        }
    }

}
