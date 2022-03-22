package com.octal.actorpayuser.ui.mobileanddth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.databinding.RowOperatorLayoutBinding

class OperatorAdapter(val list:MutableList<OperatorModel>):RecyclerView.Adapter<OperatorAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowOperatorLayoutBinding.inflate(layoutInflater,parent,false)


        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(list[position])
    }

    override fun getItemCount(): Int = list.size


    inner class MyViewHolder(val binding:RowOperatorLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bindView(item:OperatorModel){
            binding.operatorTitle.text=item.name
            binding.operatorImage.setImageResource(item.image)
        }
    }
}