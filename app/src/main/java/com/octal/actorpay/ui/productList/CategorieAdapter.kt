package com.octal.actorpay.ui.productList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.R
import com.octal.actorpay.databinding.ProductListItemBinding
import com.octal.actorpay.databinding.RowCategoriesItemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.categories.CategorieItem

class CategorieAdapter(val list:MutableList<CategorieItem>,val onClick:(Int)->Unit):RecyclerView.Adapter<CategorieAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowCategoriesItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: RowCategoriesItemBinding):RecyclerView.ViewHolder(binding.root) {
            fun bindView(categorieItem: CategorieItem){
                binding.categorieText.text=categorieItem.name
                if(categorieItem.isSelected){
                    binding.root.setBackgroundResource(R.drawable.orderstatus_bg)
                }
                else {
                    binding.root.setBackgroundResource(R.drawable.btn_outline_gray)
                }
                binding.root.setOnClickListener {
                    onClick(adapterPosition)
                }
            }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}