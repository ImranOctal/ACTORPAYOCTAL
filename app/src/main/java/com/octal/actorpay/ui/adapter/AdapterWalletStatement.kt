package com.octal.actorpay.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.AdapterWalletstatementBinding

class AdapterWalletStatement(
    private var mList: List<String>,
    private var mContext: Context
) : RecyclerView.Adapter<AdapterWalletStatement.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterWalletStatement.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterWalletstatementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterWalletStatement.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(val binding: AdapterWalletstatementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            with(binding) {
                /* Glide.with(mContext).load(data.company_logo).into(image)
                 txtNameID.setText(data.company_name)*/
            }
        }
    }
}