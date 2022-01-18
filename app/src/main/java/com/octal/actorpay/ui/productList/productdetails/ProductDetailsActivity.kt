package com.octal.actorpay.ui.productList.productdetails

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
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
import com.octal.actorpay.repositories.retrofitrepository.models.products.SingleProductResponse
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.ui.cart.CartActivity
import com.octal.actorpay.ui.cart.CartViewModel
import com.octal.actorpay.ui.productList.ProductListAdapter
import com.octal.actorpay.utils.CommonDialogsUtils
import com.techno.taskmanagement.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.core.instance.getArguments

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val productDetailsViewModel: ProductDetailsViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    lateinit var adapter: ProductListAdapter
    var isFromBuy=false
    var futureAddCart=false

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

    fun updateUI(){
        productDetailsViewModel.product.let {
            binding.productItem = productDetailsViewModel.product
            Glide.with(this)
                .load(productDetailsViewModel.product!!.image)
                .error(R.drawable.logo)
                .into(binding.productImage)

        }
        initializeCartWork()
    }

    fun initializeCartWork() {
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
                        futureAddCart=false
                        if(isFromBuy)
                            isFromBuy=false
                        if (event.message!!.code == 403) {
                            forcelogout()
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
                            is SingleProductResponse -> {
                                productDetailsViewModel.product=event.response.data
                                updateUI()
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

    fun forcelogout() {
        CommonDialogsUtils.showCommonDialog(this, cartViewModel.methodRepo, "Log Out ",
            "Session Expire", false, false, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
//                    viewModel.shared.Logout()
                    lifecycleScope.launchWhenCreated {
                        cartViewModel.methodRepo.dataStore.logOut()
                        cartViewModel.methodRepo.dataStore.setIsIntro(true)
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