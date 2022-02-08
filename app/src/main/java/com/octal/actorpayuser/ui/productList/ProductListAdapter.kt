package com.octal.actorpayuser.ui.productList

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.ProductListItemBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartItemDTO
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow

class ProductListAdapter(
    val context:Context,
    val list: MutableList<ProductItem>,
    val cartList: MutableStateFlow<MutableList<CartItemDTO>> = MutableStateFlow(mutableListOf()),
    val onClick: (position: Int, click: Clicks) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductListItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(list[position])

    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return  position
    }

    inner class MyViewHolder(val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: ProductItem) {
            binding.productItem = item
            binding.cancelPriceText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            Glide.with(binding.root)
                .load(item.image)
                .error(R.drawable.logo)
                .into(binding.productImage)
            binding.addToCart.setOnClickListener {
                onClick(adapterPosition,Clicks.AddCart)
            }
            val cart = cartList.value.find { it.productId == item.productId }
            if (cart != null) {
                binding.addToCart.text=context.getString(R.string.go_to_cart)
                binding.addToCart.setOnClickListener {
                    onClick(adapterPosition,Clicks.BuyNow)
                }
            }
            else{
                binding.addToCart.text=context.getString(R.string.add_to_cart)
            }
            binding.root.setOnClickListener {
                onClick(adapterPosition, Clicks.Root)
            }
            binding.buyNow.setOnClickListener {
                onClick(adapterPosition, Clicks.BuyNow)
            }
        }
    }

}
