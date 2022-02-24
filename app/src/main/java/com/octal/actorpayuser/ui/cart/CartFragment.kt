package com.octal.actorpayuser.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentCartBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartData
import com.octal.actorpayuser.repositories.retrofitrepository.models.cart.CartResponse
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import java.text.DecimalFormat


class CartFragment : BaseFragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by inject()

    var decimalFormat: DecimalFormat = DecimalFormat("0.00")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        cartViewModel.getCartItems()

        setAdapter()
        apiResponse()

        binding.checkout.setOnClickListener {
            if(cartViewModel.cartData!=null && cartViewModel.cartData!!.totalPrice > 0.0){

                val bundle= bundleOf("total" to cartViewModel.cartData!!.totalPrice, "subtotal" to cartViewModel.cartData!!.totalTaxableValue, "gst" to cartViewModel.cartData!!.totalSgst+cartViewModel.cartData!!.totalCgst)
                Navigation.findNavController(requireView()).navigate(R.id.placeOrderFragment,bundle)

            }
            else
            {
                showCustomToast("Cart is Empty")
            }
        }


        return binding.root
    }

    fun apiResponse() {
        lifecycleScope.launchWhenCreated {
            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        cartViewModel.responseLive.value=ResponseSealed.Empty
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
                        hideLoading()
                        showCustomToast(event.message!!.message)
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun updateUI(cartData: CartData){
        cartData.totalCgst = decimalFormat.format(cartData.totalCgst).toDouble()
        cartData.totalPrice = decimalFormat.format(cartData.totalPrice).toDouble()
        cartData.totalSgst = decimalFormat.format(cartData.totalSgst).toDouble()
        cartViewModel.cartData = cartData
        binding.gst.text = getString(R.string.rs).plus(cartData.totalCgst+cartData.totalSgst)
        binding.subTotal.text = getString(R.string.rs).plus(cartData.totalTaxableValue)
        binding.total.text = getString(R.string.rs).plus(cartData.totalPrice)
        binding.cartRecyclerview.adapter?.notifyDataSetChanged()

        if(cartViewModel.cartData!!.cartItemDTOList.size==0){
            binding.imageEmpty.visibility=View.VISIBLE
            binding.textEmpty.visibility=View.VISIBLE
            binding.totalLayout.visibility=View.GONE
            binding.imageEmpty.playAnimation()

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
        binding.cartRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerview.adapter = adapter
    }

    private fun deleteCartItemDialog(onClick: () -> Unit) {
        CommonDialogsUtils.showCommonDialog(requireActivity(),
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


}