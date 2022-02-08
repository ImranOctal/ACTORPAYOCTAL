package com.octal.actorpay.ui.mobileanddth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.RowOperatorLayoutBinding
import com.octal.actorpay.databinding.RowOrderNoteBinding

class OperatorAdapter(val list:MutableList<String>):RecyclerView.Adapter<OperatorAdapter.MyViewHolder>() {


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
        fun bindView(item:String){
            binding.operatorTitle.text=item
        }
    }
}