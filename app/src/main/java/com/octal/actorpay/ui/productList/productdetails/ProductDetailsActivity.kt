package com.octal.actorpay.ui.productList.productdetails

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.ActivityProductDetailsBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductItem
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.ui.cart.CartActivity
import com.octal.actorpay.ui.cart.CartViewModel
import com.octal.actorpay.ui.productList.ProductListAdapter
import com.octal.actorpay.utils.CommonDialogsUtils
import com.techno.taskmanagement.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val productDetailsViewModel: ProductDetailsViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    lateinit var adapter: ProductListAdapter
    var isFromBuy=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)

        setAdapter()
        cartResponse()
        apiResponse()
//        productDetailsViewModel.getProducts()


        productItem.let {
            binding.productItem = productItem
            Glide.with(this)
                .load(productItem!!.image)
                .error(R.drawable.logo)
                .into(binding.productImage)

        }

        binding.cancelPriceText.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.back.setOnClickListener {
            finish()
        }
        initializeCartWork()


    }

    fun initializeCartWork() {
        binding.addToCart.setOnClickListener {
           addToCart()
        }
        val cart = cartViewModel.cartItems.value.find { it.productId == productItem!!.productId }
        if (cart != null) {
            binding.addToCart.text = getString(R.string.go_to_cart)
            binding.addToCart.setOnClickListener {
                goToCart()
            }
        } else {
            binding.addToCart.text = getString(R.string.add_to_cart)
        }
        binding.buyNow.setOnClickListener {
            buyNow()
        }
    }

    private fun addToCart() {
        if (cartViewModel.cartItems.value.size > 0) {
            val merchantId = cartViewModel.cartItems.value[0].merchantId
            if (productItem!!.merchantId != merchantId) {
                wantsToBuyDialog {
//                                addToCart(position)
                }
            } else
                cartViewModel.addCart(productItem!!.productId)
        }
        else
        cartViewModel.addCart(productItem!!.productId)

    }
    private fun buyNow() {
        if (cartViewModel.cartItems.value.size > 0) {
            val cartItem=  cartViewModel.cartItems.value.find {
                it.productId== productItem!!.productId
            }
            if(cartItem!=null)
                goToCart()
            else{
                val merchantId = cartViewModel.cartItems.value[0].merchantId
                if (productItem!!.merchantId != merchantId) {
                    wantsToBuyDialog {
//                                    buyNow(position)
                    }
                } else {
                    isFromBuy=true
                    cartViewModel.addCart(productItem!!.productId)
                }
            }

        } else {
            isFromBuy=true
            cartViewModel.addCart(productItem!!.productId)
        }

    }

    fun goToCart() {

        val intent = Intent(this, CartActivity::class.java)
        resultLauncher.launch(intent)

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            initializeCartWork()
        }


    fun cartResponse() {
        lifecycleScope.launchWhenCreated {

            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        cartViewModel.methodRepo.showLoadingDialog(this@ProductDetailsActivity)
                    }
                    is ResponseSealed.Success -> {
//                        binding.recyclerviewSimiliarProduct.adapter?.notifyDataSetChanged()
                        if(isFromBuy) {
                            isFromBuy=false
                            goToCart()
                        }
                        initializeCartWork()
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        if(isFromBuy)
                            isFromBuy=false
                        if (event.message!!.code == 403) {
                            forcelogout(cartViewModel.methodRepo)
                        }
                        cartViewModel.methodRepo.hideLoadingDialog()
                    }
                    is ResponseSealed.Empty -> {
                        cartViewModel.methodRepo.hideLoadingDialog()

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
                        productDetailsViewModel.methodRepo.showLoadingDialog(this@ProductDetailsActivity)
                    }
                    is ResponseSealed.Success -> {
                        productDetailsViewModel.methodRepo.hideLoadingDialog()
                        when (event.response) {
                            is ProductListResponse -> {
                                productDetailsViewModel.productData.pageNumber =
                                    event.response.data.pageNumber
                                productDetailsViewModel.productData.totalPages =
                                    event.response.data.totalPages
                                productDetailsViewModel.productData.items.addAll(event.response.data.items)
                                adapter.notifyItemChanged(productDetailsViewModel.productData.items.size - 1)
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        productDetailsViewModel.methodRepo.hideLoadingDialog()
                        productDetailsViewModel.methodRepo.showCustomToast(event.message!!.message)
                    }
                    is ResponseSealed.Empty -> {
                        productDetailsViewModel.methodRepo.hideLoadingDialog()
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
                    productItem = productDetailsViewModel.productData.items[position]
                    startActivity(Intent(this, ProductDetailsActivity::class.java))
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

    fun forcelogout(methodRepo: MethodsRepo) {
        CommonDialogsUtils.showCommonDialog(this, methodRepo, "Log Out ",
            "Session Expire", false, false, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
//                    viewModel.shared.Logout()
                    lifecycleScope.launchWhenCreated {
                        methodRepo.dataStore.logOut()
                        startActivity(
                            Intent(
                                this@ProductDetailsActivity,
                                LoginActivity::class.java
                            )
                        )
                        finishAffinity()
                    }
                }

                override fun onCancel() {
                }
            })
    }

    companion object {
        var productItem: ProductItem? = null

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