package com.octal.actorpay.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.databinding.ItemFeaturesBinding
import com.octal.actorpay.ui.dashboard.`interface`.ItemListenr

class FeaturesAdapter(
    private var mList: List<String>,
    private var mContext: Context,
    private val listner:ItemListenr
) : RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFeaturesBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    /* arrayListOf("Add Money", "Send Money", "Mobile & DTH", "Utility Bill","Online Payment")*/
    override fun onBindViewHolder(holder: FeaturesAdapter.ViewHolder, position: Int) {
        holder.binding.txtTitleID.setText(mList[position])
        holder.binding.itemsLayoutID.setOnClickListener {
            listner.on_ItemClickListner(position,mList,it)

        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemFeaturesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            with(binding) {
                /* Glide.with(mContext).load(data.company_logo).into(image)
                 txtNameID.setText(data.company_name)*/
            }
        }
    }
}