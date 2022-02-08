package com.octal.actorpayuser.ui.myOrderList.placeorder

import android.content.Context
import android.view.*
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowOrderItemBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_CANCELLED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_CANCEL_ORDER
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_DELIVERED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_DISPATCHED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_FAILED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_PENDING
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_READY
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_RETURNED
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_RETURNING
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_RETURN_ORDER
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.STATUS_SUCCESS
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderItemDtos

class PlaceOrderAdapter(
    private val mContext:Context,
    private var orderList: ArrayList<OrderItemDtos>,val isFromPlaceOrder:Boolean=true,val onClick:(pos:Int,action:String)->Unit):RecyclerView.Adapter<PlaceOrderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= RowOrderItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class MyViewHolder(val binding:RowOrderItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bindView(item:OrderItemDtos)
        {
            binding.orderItem=item
            Glide.with(binding.root)
                .load(item.image)
                .error(R.drawable.logo)
                .into(binding.productImage)

            if(item.orderItemStatus == STATUS_CANCELLED ||
                item.orderItemStatus == STATUS_RETURNING ||
                item.orderItemStatus == STATUS_RETURNED ||
                item.orderItemStatus == STATUS_RETURNING ||
                item.orderItemStatus == STATUS_PENDING ||
                item.orderItemStatus == STATUS_FAILED || isFromPlaceOrder
            )
                binding.menu.visibility= View.GONE

            binding.menu.setOnClickListener {

                showPopUp(binding.menu,findStatus(item.orderItemStatus),adapterPosition)

                /*  var popupMenu= PopupMenu(binding.root.context,binding.menu)

                   popupMenu=findStatus(item.orderItemStatus)
                  popupMenu.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
                      override fun onMenuItemClick(item: MenuItem): Boolean {
                          val status=item.title.toString()
                          if(status == STATUS_CANCEL_ORDER)
                              onClick(adapterPosition,STATUS_CANCELLED)
                          else if(status == STATUS_RETURN_ORDER)
                              onClick(adapterPosition,STATUS_RETURNING)

                          return true
                      }
                  })
                  popupMenu.show()*/
           }
        }
    }

        fun showPopUp(root: View,orderStatus: String,pos:Int) {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.popup_window_order_status, null)
            val status = view.findViewById<TextView>(R.id.status)
            val rootLayout = view.findViewById<RelativeLayout>(R.id.root_layout)
            val mpopup = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            mpopup.showAsDropDown(root)
            status.text=orderStatus

            rootLayout.setOnClickListener {
                mpopup.dismiss()
                if(orderStatus == STATUS_CANCEL_ORDER)
                    onClick(pos,STATUS_CANCELLED)
                else if(orderStatus == STATUS_RETURN_ORDER)
                    onClick(pos,STATUS_RETURNING)
            }

    }

    fun findStatus(status:String):String{

        if(status == STATUS_SUCCESS || status == STATUS_READY)
            return  STATUS_CANCEL_ORDER
        else if(status == STATUS_DISPATCHED || status == STATUS_DELIVERED)
            return  STATUS_RETURN_ORDER
        return status

    }

}