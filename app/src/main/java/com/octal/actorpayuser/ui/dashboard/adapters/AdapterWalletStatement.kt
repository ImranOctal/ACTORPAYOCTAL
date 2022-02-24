package com.octal.actorpayuser.ui.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.AdapterWalletstatementBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletData

class AdapterWalletStatement(val context :Context,val items: MutableList<WalletData>,val methodsRepo: MethodsRepo,val onClick:(position:Int)->Unit) : RecyclerView.Adapter<AdapterWalletStatement.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterWalletstatementBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: AdapterWalletstatementBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bindView(item:WalletData,position: Int){
                binding.rowWalletText.text=item.transactionRemark
                binding.rowWalletTxn.text=item.walletTransactionId
                binding.rowWalletDate.text=methodsRepo.getFormattedOrderDate(item.createdAt)

                binding.root.setOnClickListener {
                    onClick(position)
                }
                if(item.transactionTypes == "DEBIT"){
                    binding.rowWalletAmount.setTextColor(ContextCompat.getColor(context, R.color.pink_color))
                    binding.rowWalletAmount.text="- ₹ "+item.transactionAmount.toString()
                }
                if(item.transactionTypes == "CREDIT"){
                    binding.rowWalletAmount.setTextColor(ContextCompat.getColor(context, R.color.green_color))
                    binding.rowWalletAmount.text="+ ₹ "+item.transactionAmount.toString()
                }
            }
        }
}