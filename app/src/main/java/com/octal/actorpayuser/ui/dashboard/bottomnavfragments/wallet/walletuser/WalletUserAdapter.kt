package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.wallet.walletuser

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowWalletUserCreditBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletData

class WalletUserAdapter(val context :Context, val items: MutableList<WalletData>, val methodsRepo: MethodsRepo, val onClick:(position:Int)->Unit) : RecyclerView.Adapter<WalletUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowWalletUserCreditBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: RowWalletUserCreditBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bindView(item:WalletData,position: Int){
                binding.rowWalletText.text=item.transactionRemark.replace(","," ")
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

                if(item.purchaseType == "TRANSFER"){
//                    binding.rowWalletText.text="Money Transferred Successfully"
                }
                else if(item.purchaseType == "SHOPPING"){
//                    binding.rowWalletText.text="Online Shopping"
                }
            }
        }
}