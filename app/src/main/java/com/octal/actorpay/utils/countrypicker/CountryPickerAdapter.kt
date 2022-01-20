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
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    inner class MyViewHolder(private val countryListItemBinding: CountryListitemBinding):RecyclerView.ViewHolder(countryListItemBinding.root) {
            fun bindView(item: CountryItem){
                countryListItemBinding.countryItem=item
                countryListItemBinding.root.setOnClickListener {
                    onClick(adapterPosition)
                }
            }
    }
}