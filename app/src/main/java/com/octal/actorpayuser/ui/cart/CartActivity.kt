package com.octal.actorpayuser.ui.cart

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.ActivityCartBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartData
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartResponse
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import android.view.View
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.ui.myOrderList.placeorder.PlaceOrderActivity


class CartActivity : BaseActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel: CartViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        setAdapter()
        apiResponse()
        cartViewModel.getCartItems()


        binding.back.setOnClickListener {
            finish()
        }
        binding.checkout.setOnClickListener {
            if(cartViewModel.cartData!=null && cartViewModel.cartData!!.totalPrice > 0.0){
                PlaceOrderActivity.total=cartViewModel.cartData!!.totalPrice
                PlaceOrderActivity.subTotal=cartViewModel.cartData!!.totalTaxableValue
                PlaceOrderActivity.gst=cartViewModel.cartData!!.totalSgst+cartViewModel.cartData!!.totalCgst
                startActivity(Intent(this, PlaceOrderActivity::class.java))
            }
            else
            {
                showCustomToast("Cart is Empty")
            }
        }
    }


    fun apiResponse() {
        lifecycleScope.launchWhenCreated {
            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        hideLoadingDialog()
                        when (event.response) {
                            is CartResponse -> {
                                event.response.data.let {
                                    updateUI(it)
                                }
                            }
                            is ResponseSealed.Success -> {
                                binding.cartRecyclerview.adapter?.notifyDataSetChanged()
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

    private fun updateUI(cartData: CartData){
        cartViewModel.cartData = cartData
        binding.gst.text = getString(R.string.rs).plus(cartData.totalCgst+cartData.totalSgst)
        binding.subTotal.text = getString(R.string.rs).plus(cartData.totalTaxableValue)
        binding.total.text = getString(R.string.rs).plus(cartData.totalPrice)
        binding.cartRecyclerview.adapter?.notifyDataSetChanged()

        if(cartViewModel.cartData!!.cartItemDTOList.size==0){
            binding.imageEmpty.visibility=View.VISIBLE
            binding.textEmpty.visibility=View.VISIBLE
            binding.totalLayout.visibility=View.GONE

        }
        else{
            binding.imageEmpty.visibility=View.GONE
            binding.textEmpty.visibility=View.GONE
            binding.totalLayout.visibility=View.VISIBLE
        }
    }

    fun setAdapter() {
       val adapter = CartAdapter(cartViewModel.cartItems){
            position, clicks ->
            val currentCart=cartViewModel.cartItems.value[position]
            when(clicks){
                Clicks.Delete->{
                    deleteCartItemDialog {
                        cartViewModel.deleteCart(currentCart.cartItemId)
                    }
                }
                Clicks.Minus->{
                   if(currentCart.productQty == 1)
                       deleteCartItemDialog {
                           cartViewModel.deleteCart(currentCart.cartItemId)
                       }
                    else
                        cartViewModel.updateCart(currentCart.cartItemId,currentCart.productQty-1)
                }
                Clicks.Plus->{
                    cartViewModel.updateCart(currentCart.cartItemId,currentCart.productQty+1)
                }
                else ->Unit
            }
        }
        binding.cartRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerview.adapter = adapter
    }

    private fun deleteCartItemDialog(onClick: () -> Unit) {
        CommonDialogsUtils.showCommonDialog(this,
            cartViewModel.methodRepo,
            "Delete Item",
            "Are You Sure?",
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

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK)
        finish()
    }

}