package com.octal.actorpayuser.ui.productList

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.ProductListItemBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartItemDTO
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.DecimalFormat

class ProductPagingAdapter(
    val context: Context,
    val cartList: MutableStateFlow<MutableList<CartItemDTO>> = MutableStateFlow(mutableListOf()),
    val onClick: ( click: Clicks,productItem:ProductItem) -> Unit
) : PagingDataAdapter<ProductItem, ProductPagingAdapter.ProductPageViewHolder>(CharacterComparator) {

    var decimalFormat: DecimalFormat = DecimalFormat("0.00")

    override fun onBindViewHolder(holder: ProductPageViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it,position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPageViewHolder =
        ProductPageViewHolder(
            ProductListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

  inner class ProductPageViewHolder(val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductItem,position: Int) = with(binding) {
            binding.productItem = item
            binding.cancelPriceText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            binding.actualPriceText.text="₹"+decimalFormat.format((item.dealPrice+item.sgst+item.cgst))
            binding.cancelPriceText.text="₹"+decimalFormat.format((item.actualPrice))
            Glide.with(binding.root)
                .load(item.image)
                .error(R.drawable.logo)
                .into(binding.productImage)

            if(item.stockCount>0)
            {
                binding.addToCart.visibility= View.VISIBLE
                binding.buyNow.visibility= View.VISIBLE
                binding.outOfStock.visibility= View.GONE
            }
            else{
                binding.addToCart.visibility= View.GONE
                binding.buyNow.visibility= View.GONE
                binding.outOfStock.visibility= View.VISIBLE
            }
            binding.addToCart.setOnClickListener {
                onClick(Clicks.AddCart,item)
            }
            val cart = cartList.value.find { it.productId == item.productId }
            if (cart != null) {
                binding.addToCart.text=context.getString(R.string.go_to_cart)
                binding.addToCart.setOnClickListener {
                    onClick(Clicks.BuyNow,item)
                }
            }
            else{
                binding.addToCart.text=context.getString(R.string.add_to_cart)
            }
            binding.root.setOnClickListener {
                onClick( Clicks.Root,item)
            }
            binding.buyNow.setOnClickListener {
                onClick( Clicks.BuyNow,item)
            }
        }
    }


    object CharacterComparator : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem) =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem) =
            oldItem == newItem
    }


}