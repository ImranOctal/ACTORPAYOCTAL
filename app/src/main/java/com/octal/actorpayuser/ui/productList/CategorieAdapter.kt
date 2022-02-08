package com.octal.actorpayuser.ui.productList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowCategoriesItemBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieItem

class CategoryAdapter(val list:MutableList<CategorieItem>,val onClick:(position:Int)->Unit):RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {



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
            fun bindView(categoriesItem: CategorieItem){
                binding.categorieText.text=categoriesItem.name
                if(categoriesItem.isSelected){
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