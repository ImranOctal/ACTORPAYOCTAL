package com.octal.actorpay.ui.productList.productsfilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.FilterCustomizationListItemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.categories.SubCategorieItem

class ProductFilterSubCategoryAdapter(val list: MutableList<SubCategorieItem>):RecyclerView.Adapter<ProductFilterSubCategoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterCustomizationListItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: FilterCustomizationListItemBinding):RecyclerView.ViewHolder(binding.root) {
            fun bindView(categoriesItem: SubCategorieItem){
                binding.name=categoriesItem.name
            }
    }

}