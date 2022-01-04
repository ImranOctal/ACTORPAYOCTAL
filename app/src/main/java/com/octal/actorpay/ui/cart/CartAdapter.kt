package com.octal.actorpay.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpay.R
import com.octal.actorpay.databinding.RowCartItemBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartItemDTO
import kotlinx.coroutines.flow.MutableStateFlow

class CartAdapter(val cartList: MutableStateFlow<MutableList<CartItemDTO>> = MutableStateFlow(mutableListOf()),val onClick:(position:Int,clicks:Clicks)->Unit):RecyclerView.Adapter<CartAdapter.MyViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val layoutInflater=LayoutInflater.from(parent.context)
            val binding=RowCartItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewholder(binding)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        holder.bindView(cartList.value[position])
    }

    override fun getItemCount(): Int {
       return cartList.value.size

    }



     inner class MyViewholder(val binding:RowCartItemBinding):RecyclerView.ViewHolder(binding.root) {

         fun bindView(item:CartItemDTO){
                binding.cartItem=item
             Glide.with(binding.root)
                 .load(item.image)
                 .error(R.drawable.logo)
                 .into(binding.productImage)

                binding.productQuantityDelete.setOnClickListener {
                    onClick(adapterPosition,Clicks.Delete)
                }
             binding.productQuantityMinus.setOnClickListener {
                 onClick(adapterPosition,Clicks.Minus)
             }
             binding.productQuantityPlus.setOnClickListener {
                 onClick(adapterPosition,Clicks.Plus)
             }
         }
    }


}