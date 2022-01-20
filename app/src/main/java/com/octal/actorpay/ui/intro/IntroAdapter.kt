package com.octal.actorpay.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.RowIntroLayoutBinding
import com.octal.actorpay.repositories.AppConstance.Clicks

class IntroAdapter(val list: MutableList<IntroModel>,val onClick:(action:Clicks,position:Int)->Unit):RecyclerView.Adapter<IntroAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowIntroLayoutBinding.inflate(layoutInflater,parent,false)


        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    inner class MyViewHolder(val binding: RowIntroLayoutBinding):RecyclerView.ViewHolder(binding.root) {
            fun bindView(item:IntroModel){
                binding.intromodel=item

                when (adapterPosition) {
                    0 -> {
                        binding.next.visibility=View.VISIBLE
                        binding.prev.visibility=View.GONE
                        binding.getStart.visibility=View.GONE
                    }
                    1 -> {
                        binding.next.visibility=View.VISIBLE
                        binding.prev.visibility=View.VISIBLE
                        binding.getStart.visibility=View.GONE
                    }
                    2 -> {
                        binding.next.visibility=View.VISIBLE
                        binding.prev.visibility=View.VISIBLE
                        binding.getStart.visibility=View.GONE
                    }
                    3 -> {
                        binding.next.visibility=View.VISIBLE
                        binding.prev.visibility=View.VISIBLE
                        binding.getStart.visibility=View.GONE
                    }
                    4 -> {
                        binding.next.visibility=View.GONE
                        binding.prev.visibility=View.VISIBLE
                        binding.getStart.visibility=View.VISIBLE
                    }
                }

                binding.skip.setOnClickListener {
                    onClick(Clicks.Skip,adapterPosition)
                }

                binding.next.setOnClickListener {
                    onClick(Clicks.Next,adapterPosition)
                }

                binding.prev.setOnClickListener {
                    onClick(Clicks.Prev,adapterPosition)
                }

                binding.getStart.setOnClickListener {
                    onClick(Clicks.GetStart,adapterPosition)
                }
            }
    }
}