package com.octal.actorpayuser.ui.dispute.disputedetails

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
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
            holder.bindView(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner  class MyViewHolder(val binding: RowDisputeMessageLayoutBinding):RecyclerView.ViewHolder(binding.root) {

        fun bindView(itemPos:Int){
            val item=list[itemPos]
            val scale: Float = mContext.resources.displayMetrics.density
            val dpAsPixels = (50 * scale + 0.5f).toInt()

            binding.message.text=item.message
            binding.date.text=methodsRepo.getFormattedOrderDate(item.createdAt)
//            val layoutParams = binding.message.getLayoutParams() as ViewGroup.MarginLayoutParams

            if(item.userType == "customer")
                {
                    binding.message.setBackgroundResource(R.drawable.bg_message_primary)
//                    binding.messageLayout.setPadding(dpAsPixels,0,0,0)
//                    layoutParams.marginStart = dpAsPixels
                    binding.user.text="You"
                    binding.user.setTextColor(ContextCompat.getColor(mContext,R.color.primary))
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.END
                        marginStart = dpAsPixels
                    }
                    binding.user.layoutParams=params
                    binding.date.layoutParams=params
                    binding.message.layoutParams=params
                }
            else{

                binding.message.setBackgroundResource(R.drawable.bg_message_pink)
//                binding.messageLayout.setPadding(0,0,dpAsPixels,0)
//                layoutParams.marginEnd = dpAsPixels
                binding.user.text="Merchant"
                binding.user.setTextColor(ContextCompat.getColor(mContext,R.color.pink_color))
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.START
                    marginEnd = dpAsPixels
                }
                binding.user.layoutParams=params
                binding.date.layoutParams=params
                binding.message.layoutParams=params

                if(item.userType == "admin"){
                    binding.user.text="Admin"
                    binding.message.setBackgroundResource(R.drawable.bg_message_green)
                    binding.user.setTextColor(ContextCompat.getColor(mContext,R.color.green_color))
                }
            }

            if(itemPos+1<list.size){
                val oldUser=list[itemPos+1].userType
                if(oldUser==item.userType){
                    binding.user.visibility= View.GONE
                }
            }

//            binding.message.layoutParams=layoutParams
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}