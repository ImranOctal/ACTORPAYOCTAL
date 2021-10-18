package com.octal.actorpay.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.AdapterTransactionBinding

class TransactionAdapter(private var mList: List<String>,
                         private var mContext: Context
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: AdapterTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            with(binding) {
                /* Glide.with(mContext).load(data.company_logo).into(image)
                 txtNameID.setText(data.company_name)*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterTransactionBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 4
    }
}