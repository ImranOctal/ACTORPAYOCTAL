package com.octal.actorpayuser.ui.dispute.disputedetails

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowDisputeMessageLayoutBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.DisputeMessage


class DisputeMessageAdapter(val mContext: Context,val methodsRepo: MethodsRepo, val list:MutableList<DisputeMessage>):RecyclerView.Adapter<DisputeMessageAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowDisputeMessageLayoutBinding.inflate(layoutInflater, parent, false)


        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner  class MyViewHolder(val binding: RowDisputeMessageLayoutBinding):RecyclerView.ViewHolder(binding.root) {

        fun bindView(item:DisputeMessage){
            val scale: Float = mContext.resources.displayMetrics.density
            val dpAsPixels = (50 * scale + 0.5f).toInt()

            binding.message.text=item.message + item.message + item.message + item.message
            binding.date.text=methodsRepo.getFormattedOrderDate(item.createdAt)

            if(item.userType == "customer")
                {
                    binding.message.setBackgroundResource(R.drawable.bg_outline_gray)
                    binding.messageLayout.setPadding(0,0,dpAsPixels,0)
                    binding.user.text="You"
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.START
                    }
                    binding.user.layoutParams=params
                }
            else{
                binding.message.setBackgroundResource(R.drawable.orderstatus_bg)
                binding.messageLayout.setPadding(dpAsPixels,0,0,0)
                binding.user.text=item.userType
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END
                }
                binding.user.layoutParams=params
            }
        }
    }

}