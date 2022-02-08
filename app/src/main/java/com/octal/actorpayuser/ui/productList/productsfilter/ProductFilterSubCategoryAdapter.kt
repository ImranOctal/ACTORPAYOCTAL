package com.octal.actorpayuser.ui.productList.productsfilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.databinding.FilterCustomizationListItemBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.SubCategorieItem

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