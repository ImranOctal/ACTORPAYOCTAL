package com.octal.actorpayuser.ui.productList.productdetails

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.ActivityProductDetailsBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpayuser.ui.cart.CartActivity
import com.octal.actorpayuser.ui.cart.CartViewModel
import com.octal.actorpayuser.ui.productList.ProductListAdapter
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class ProductDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val productDetailsViewModel: ProductDetailsViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    lateinit var adapter: ProductListAdapter
    private var isFromBuy=false
    private var futureAddCart=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this,R.color.primary)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)

        setAdapter()
        cartResponse()
        apiResponse()
        var id=""
        if(intent!=null){
            if(intent.hasExtra("id"))
                id=intent.getStringExtra("id")!!
        }
        productDetailsViewModel.getProductById(id)

        binding.cancelPriceText.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.back.setOnClickListener {
            finish()
        }



    }

    private fun updateUI(){
        productDetailsViewModel.product.let {
            binding.productItem = productDetailsViewModel.product
            Glide.with(this)
                .load(productDetailsViewModel.product!!.image)
                .error(R.drawable.logo)
                .into(binding.productImage)

        }
        initializeCartWork()
    }

    private fun initializeCartWork() {
        binding.addToCart.setOnClickListener {
           addToCart()
        }
        if(productDetailsViewModel.product != null) {
            val cart =
                cartViewModel.cartItems.value.find { it.productId == productDetailsViewModel.product!!.productId }
            if (cart != null) {
                binding.addToCart.text = getString(R.string.go_to_cart)
                binding.addToCart.setOnClickListener {
                    goToCart()
                }
            } else {
                binding.addToCart.text = getString(R.string.add_to_cart)
            }
        }
        binding.buyNow.setOnClickListener {
            buyNow()
        }
    }

    private fun addToCart() {
        if (cartViewModel.cartItems.value.size > 0) {
            val merchantId = cartViewModel.cartItems.value[0].merchantId
            if (productDetailsViewModel.product!!.merchantId != merchantId) {
                wantsToBuyDialog {
                    futureAddCart=true
                    cartViewModel.deleteAllCart()

                }
            } else
                cartViewModel.addCart(productDetailsViewModel.product!!.productId,productDetailsViewModel.product!!.dealPrice)
        }
        else
        cartViewModel.addCart(productDetailsViewModel.product!!.productId,productDetailsViewModel.product!!.dealPrice)

    }
    private fun buyNow() {
        if (cartViewModel.cartItems.value.size > 0) {
            val cartItem=  cartViewModel.cartItems.value.find {
                it.productId== productDetailsViewModel.product!!.productId
            }
            if(cartItem!=null)
                goToCart()
            else{
                val merchantId = cartViewModel.cartItems.value[0].merchantId
                if (productDetailsViewModel.product!!.merchantId != merchantId) {
                    wantsToBuyDialog {
                        isFromBuy=true
                        futureAddCart=true
                        cartViewModel.deleteAllCart()
                    }
                } else {
                    isFromBuy=true
                    cartViewModel.addCart(productDetailsViewModel.product!!.productId,productDetailsViewModel.product!!.dealPrice)
                }
            }

        } else {
            isFromBuy=true
            cartViewModel.addCart(productDetailsViewModel.product!!.productId,productDetailsViewModel.product!!.dealPrice)
        }

    }

    private fun goToCart() {

        val intent = Intent(this, CartActivity::class.java)
        resultLauncher.launch(intent)

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            initializeCartWork()
        }


    private fun cartResponse() {
        lifecycleScope.launchWhenCreated {

            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        if(futureAddCart){
                            addToCart()
                            futureAddCart=false
                        }
                        if(isFromBuy) {
                            isFromBuy=false
                            goToCart()
                        }
                        initializeCartWork()
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoadingDialog()
                        futureAddCart=false
                        if(isFromBuy)
                            isFromBuy=false
                        if (event.message!!.code == 403) {
                            forcelogout(productDetailsViewModel.methodRepo)
                        }
                    }
                    is ResponseSealed.Empty -> {
                        hideLoadingDialog()

                    }

                }
            }
        }
    }

    fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            productDetailsViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        hideLoadingDialog()
                        when (event.response) {
                            is ProductListResponse -> {
                                productDetailsViewModel.productData.pageNumber =
                                    event.response.data.pageNumber
                                productDetailsViewModel.productData.totalPages =
                                    event.response.data.totalPages
                                productDetailsViewModel.productData.items.addAll(event.response.data.items)
                                adapter.notifyItemChanged(productDetailsViewModel.productData.items.size - 1)
                            }
                            is SingleProductResponse -> {
                                productDetailsViewModel.product=event.response.data
                                updateUI()
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoadingDialog()
                        showCustomToast(event.message!!.message)
                    }
                    is ResponseSealed.Empty -> {
                        hideLoadingDialog()
                    }
                }
            }
        }
    }


    fun setAdapter() {

        adapter = ProductListAdapter(
            this,
            productDetailsViewModel.productData.items,
            cartViewModel.cartItems
        ) { position, click ->
            when (click) {
                Clicks.Root -> {
                    val intent=Intent(this,ProductDetailsActivity::class.java)
                    intent.putExtra("id",productDetailsViewModel.productData.items[position].productId)
                    startActivity(intent)
                }
                else -> Unit
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerviewSimiliarProduct.layoutManager = layoutManager
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (productDetailsViewModel.productData.pageNumber < productDetailsViewModel.productData.totalPages - 1) {
                        productDetailsViewModel.productData.pageNumber += 1
                        productDetailsViewModel.getProducts()
                    }
                }
            }
        binding.recyclerviewSimiliarProduct.addOnScrollListener(endlessRecyclerViewScrollListener)
        binding.recyclerviewSimiliarProduct.adapter = adapter


    }

    private fun wantsToBuyDialog(onClick: () -> Unit) {
        CommonDialogsUtils.showCommonDialog(this,
            cartViewModel.methodRepo,
            "Replace Cart Item",
            "Your Cart Contains Products from Different Merchant, Do you want to discard the selection and add this product?",
            autoCancelable = true,
            isCancelAvailable = true,
            isOKAvailable = true,
            showClickable = false,
            callback = object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    onClick()
                }

                override fun onCancel() {

                }
            })
    }

}