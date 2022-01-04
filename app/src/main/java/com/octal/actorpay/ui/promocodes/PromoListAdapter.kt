package com.octal.actorpay.ui.promocodes

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
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService




class PromoListAdapter(
    val context:Context,
    val list: MutableList<PromoItem>,
    val onCopy:(text:String)->Unit
) : RecyclerView.Adapter<PromoListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PromoListItemBinding.inflate(inflater, parent, false)
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

    inner class MyViewHolder(val binding: PromoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: PromoItem) {
            binding.promoItem = item

         /*   Glide.with(binding.root)
                .load(item.image)
                .error(R.drawable.logo)
                .into(binding.productImage)*/
            binding.copy.setOnClickListener {
               onCopy(binding.promoCode.text.toString())
            }

        }
    }

}
