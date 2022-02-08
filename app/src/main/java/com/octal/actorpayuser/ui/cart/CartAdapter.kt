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

class CartAdapter(private val cartList: MutableStateFlow<MutableList<CartItemDTO>> = MutableStateFlow(mutableListOf()), val onClick:(position:Int, clicks:Clicks)->Unit):RecyclerView.Adapter<CartAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
            val binding=RowCartItemBinding.inflate(layoutInflater,parent,false)

        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(cartList.value[position])
    }

    override fun getItemCount(): Int {
       return cartList.value.size

    }



     inner class MyViewHolder(val binding:RowCartItemBinding):RecyclerView.ViewHolder(binding.root) {

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