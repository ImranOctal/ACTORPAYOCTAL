package com.octal.actorpay.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.ActivityIntroBinding
import com.octal.actorpay.databinding.RowIntroLayoutBinding
import com.octal.actorpay.databinding.RowOrderListItemBinding

class IntroAdapter(val list: MutableList<IntroModel>,val onClick:(action:String,position:Int)->Unit):RecyclerView.Adapter<IntroAdapter.MyViewHolder>(){


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

                if(adapterPosition==0){
                    binding.next.visibility=View.VISIBLE
                    binding.prev.visibility=View.GONE
                    binding.getStart.visibility=View.GONE
                }
                else if(adapterPosition==1){
                    binding.next.visibility=View.VISIBLE
                    binding.prev.visibility=View.VISIBLE
                    binding.getStart.visibility=View.GONE
                }
                else if(adapterPosition==2){
                    binding.next.visibility=View.VISIBLE
                    binding.prev.visibility=View.VISIBLE
                    binding.getStart.visibility=View.GONE
                }
                else if(adapterPosition==3){
                    binding.next.visibility=View.VISIBLE
                    binding.prev.visibility=View.VISIBLE
                    binding.getStart.visibility=View.GONE
                }
                else if(adapterPosition==4){
                    binding.next.visibility=View.GONE
                    binding.prev.visibility=View.VISIBLE
                    binding.getStart.visibility=View.VISIBLE
                }

                binding.skip.setOnClickListener {
                    onClick("skip",adapterPosition)
                }

                binding.next.setOnClickListener {
                    onClick("next",adapterPosition)
                }

                binding.prev.setOnClickListener {
                    onClick("prev",adapterPosition)
                }

                binding.getStart.setOnClickListener {
                    onClick("getstart",adapterPosition)
                }
            }
    }
}