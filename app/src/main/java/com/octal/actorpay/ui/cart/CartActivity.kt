package com.octal.actorpay.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.ActivityCartBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartData
import com.octal.actorpay.repositories.retrofitrepository.models.cart.CartResponse
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import android.view.View
import android.widget.Toast
import com.octal.actorpay.MainActivity
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderResponse
import com.octal.actorpay.ui.myOrderList.placeorder.PlaceOrderActivity
import com.octal.actorpay.ui.myOrderList.placeorder.PlaceOrderDialog


class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    lateinit var adapter: CartAdapter
    val cartViewModel: CartViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        setAdapter()
        cartResponse()
        apiResponse()
        cartViewModel.getCartItmes()


        binding.back.setOnClickListener {
            finish()
        }
        binding.checkout.setOnClickListener {
            if(cartViewModel.cartData!=null && cartViewModel.cartData!!.totalPrice > 0.0){
                PlaceOrderActivity.total=cartViewModel.cartData!!.totalPrice
                PlaceOrderActivity.subTotal=cartViewModel.cartData!!.totalTaxableValue
                PlaceOrderActivity.gst=cartViewModel.cartData!!.totalSgst+cartViewModel.cartData!!.totalCgst
                startActivity(Intent(this, PlaceOrderActivity::class.java))
//                cartViewModel.placeOrder()
            }
            else
            {
                Toast.makeText(this,"Cart is Empty",Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun cartResponse() {
        lifecycleScope.launchWhenCreated {

            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        cartViewModel.methodRepo.showLoadingDialog(this@CartActivity)
                    }
                    is ResponseSealed.Success -> {
                        binding.cartRecyclerview.adapter?.notifyDataSetChanged()
                    }
                    is ResponseSealed.ErrorOnResponse -> {
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
        lifecycleScope.launchWhenCreated {
            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        cartViewModel.methodRepo.showLoadingDialog(this@CartActivity)
                    }
                    is ResponseSealed.Success -> {
                        cartViewModel.methodRepo.hideLoadingDialog()
                        when (event.response) {
                            is CartResponse -> {

                                event.response.data.let {
                                    updateUI(it)
                                }

                            }
                            is PlaceOrderResponse->{
                                cartViewModel.getCartItmes()
                                PlaceOrderDialog(this@CartActivity,cartViewModel.methodRepo,true,event.response.data){
                                    if(it.equals("done")) {
                                        startActivity(
                                            Intent(
                                                this@CartActivity,
                                                MainActivity::class.java
                                            )
                                        )
                                        finishAffinity()
                                    }
                                }.show(supportFragmentManager,"Place")
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        cartViewModel.methodRepo.hideLoadingDialog()
                        cartViewModel.methodRepo.showCustomToast(event.message!!.message)
                    }
                    is ResponseSealed.Empty -> {
                        cartViewModel.methodRepo.hideLoadingDialog()
                    }
                }
            }
        }

    }

    fun updateUI(cartData: CartData){
        cartViewModel.cartData = cartData
        binding.gst.text = getString(R.string.rs).plus(cartData.totalCgst+cartData.totalSgst)
        binding.subTotal.text = getString(R.string.rs).plus(cartData.totalTaxableValue)
        binding.total.text = getString(R.string.rs).plus(cartData.totalPrice)
        adapter.notifyItemChanged(cartViewModel.cartItems.value.size)

        if(cartViewModel.cartData!!.cartItemDTOList.size==0){
            binding.imageEmpty.visibility=View.VISIBLE
            binding.textEmpty.visibility=View.VISIBLE
        }
        else{
            binding.imageEmpty.visibility=View.GONE
            binding.textEmpty.visibility=View.GONE
        }
    }

    fun setAdapter() {
        adapter = CartAdapter(cartViewModel.cartItems){
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


    fun forcelogout(methodRepo: MethodsRepo){
        CommonDialogsUtils.showCommonDialog(this,methodRepo, "Log Out ",
            "Session Expire", false, false, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    lifecycleScope.launchWhenCreated {
                        methodRepo.dataStore.logOut()
                        methodRepo.dataStore.setIsIntro(true)
                        startActivity(Intent(this@CartActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
                override fun onCancel() {
                }
            })
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

    private fun orderPlacedDialog(){

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK)
        finish()
    }

}