package com.octal.actorpay.utils.countrypicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.CountryListitemBinding
import com.octal.actorpay.repositories.retrofitrepository.models.misc.CountryItem

class CountryPickerAdapter(val list:MutableList<CountryItem>,val onClick:(position:Int)->Unit):RecyclerView.Adapter<CountryPickerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryListitemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(list.get(position))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    inner class MyViewHolder(val countryListitemBinding: CountryListitemBinding):RecyclerView.ViewHolder(countryListitemBinding.root) {
            fun bindView(item: CountryItem){
                countryListitemBinding.countryItem=item
                countryListitemBinding.root.setOnClickListener {
                    onClick(adapterPosition)
                }
            }
    }
}