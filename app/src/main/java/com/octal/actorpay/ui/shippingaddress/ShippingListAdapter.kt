package com.octal.actorpay.ui.shippingaddress

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
import android.widget.PopupMenu
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import com.octal.actorpay.databinding.ShippingListItemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem


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
        holder.bindView(list[position])

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

        fun bindView(item: ShippingAddressItem) {
            binding.shippingItem = item

//            binding.selectRadio.isChecked=item.isSelect
            /*binding.options.setOnClickListener {
                val popupMenu=PopupMenu(binding.root.context,binding.options)
                popupMenu.menu.add("Edit")
                popupMenu.menu.add("Delete")
                popupMenu.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        if(item.title.equals("Edit")){
                            onClick(adapterPosition,"Edit")
                        }
                        else if(item.title.equals("Delete")){
                            onClick(adapterPosition,"Delete")
                        }
                        return true
                    }
                })
                popupMenu.show()
            }*/
            binding.delete.setOnClickListener {
                onClick(adapterPosition,"Delete")
            }
            binding.deleteIcon.setOnClickListener {
                onClick(adapterPosition,"Delete")
            }
            binding.edit.setOnClickListener {
                onClick(adapterPosition,"Edit")
            }
            binding.editIcon.setOnClickListener {
                onClick(adapterPosition,"Edit")
            }

         /*   Glide.with(binding.root)
                .load(item.image)
                .error(R.drawable.logo)
                .into(binding.productImage)*/


        }
    }

}
