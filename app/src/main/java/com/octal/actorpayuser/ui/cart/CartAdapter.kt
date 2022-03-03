package com.octal.actorpayuser.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RowCartItemBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartItemDTO
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.DecimalFormat

class CartAdapter(private val cartList: MutableStateFlow<MutableList<CartItemDTO>> = MutableStateFlow(mutableListOf()), val onClick:(position:Int, clicks:Clicks)->Unit):RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    var decimalFormat: DecimalFormat = DecimalFormat("0.00")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
            val binding=RowCartItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(cartList.value[position],position)
    }

    override fun getItemCount(): Int {
       return cartList.value.size

    }



     inner class MyViewHolder(val binding:RowCartItemBinding):RecyclerView.ViewHolder(binding.root) {

         fun bindView(item:CartItemDTO,position: Int){
                binding.cartItem=item
             Glide.with(binding.root)
                 .load(item.image)
                 .error(R.drawable.logo)
                 .into(binding.productImage)

             binding.actualPriceText.text=" ₹".plus(item.productPrice)
             binding.actualPricetax.text="(including ".plus(decimalFormat.format(item.taxPercentage)).plus("% gst)")

                binding.productQuantityDelete.setOnClickListener {
                    onClick(position,Clicks.Delete)
                }
             binding.productQuantityMinus.setOnClickListener {
                 onClick(position,Clicks.Minus)
             }
             binding.productQuantityPlus.setOnClickListener {
                 onClick(position,Clicks.Plus)
             }
         }
    }


}