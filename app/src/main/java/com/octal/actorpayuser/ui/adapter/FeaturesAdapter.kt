package com.octal.actorpayuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.databinding.ItemFeaturesBinding

class FeaturesAdapter(
    private var mList: List<String>,
    private val onClick:(position:Int)->Unit
) : RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFeaturesBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeaturesAdapter.ViewHolder, position: Int) {
        holder.binding.txtTitleID.text = mList[position]
        holder.binding.itemsLayoutID.setOnClickListener {
            onClick(position)

        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemFeaturesBinding) :
        RecyclerView.ViewHolder(binding.root)

}