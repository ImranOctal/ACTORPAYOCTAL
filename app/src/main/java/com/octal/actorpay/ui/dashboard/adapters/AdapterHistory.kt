package com.octal.actorpay.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.AdapterHistoryItemBinding

class AdapterHistory : RecyclerView.Adapter<AdapterHistory.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterHistoryItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 5
    }
    inner class ViewHolder(val binding: AdapterHistoryItemBinding) : RecyclerView.ViewHolder(binding.root)


}