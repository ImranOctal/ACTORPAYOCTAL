package com.octal.actorpay.ui.myOrderList.placeorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.ActivityPlaceOrderBinding
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderParamas
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderResponse
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.ui.cart.CartViewModel
import com.octal.actorpay.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class PlaceOrderActivity : AppCompatActivity() {
    val placeOrderViewModel: PlaceOrderViewModel by inject()
    private lateinit var binding: ActivityPlaceOrderBinding

    companion object{
        var total=0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_order)
        cartResponse()

        binding.checkout.text="Pay ₹$total"



        binding.checkout.setOnClickListener {
//            cartViewModel.placeOrder()
            validate()
        }
    }


    fun validate(){
        val add_line_1=binding.addressLine1.text.toString().trim()
        val add_line_2=binding.addressLine2.text.toString().trim()
        val zipcode=binding.addressZipcode.text.toString().trim()
        val city=binding.addressCity.text.toString().trim()
        val state=binding.addressState.text.toString().trim()
        val country=binding.addressCountry.text.toString().trim()
        val p_contact=binding.addressPrimaryContact.text.toString().trim()
        val s_contact=binding.addressSecondaryContact.text.toString().trim()
        var isValid=true
        if(add_line_1.equals("")){
            binding.addressLine1.error="Please Enter Address Line 1"
            isValid=false
        }
        if(zipcode.length<6){
            binding.addressZipcode.error="Please Enter Valid Zipcode"
            isValid=false
        }
        if(city.equals("")){
            binding.addressCity.error="Please Enter City"
            isValid=false
        }
        if(state.equals("")){
            binding.addressState.error="Please Enter State"
            isValid=false
        }
        if(country.equals("")){
            binding.addressCountry.error="Please Enter Country"
            isValid=false
        }
        if(p_contact.length<5){
            binding.addressPrimaryContact.error="Please Enter Valid Contact"
            isValid=false
        }
        if(s_contact.length<5){
            binding.addressSecondaryContact.error="Please Enter Valid Contact"
            isValid=false
        }
        if(isValid){
//            Toast.makeText(this,"done",Toast.LENGTH_SHORT).show()
            placeOrderViewModel.placeOrder(PlaceOrderParamas(add_line_1,add_line_2,zipcode,city,state,country,p_contact,s_contact))
        }
    }



    fun cartResponse() {
        lifecycleScope.launchWhenCreated {

            placeOrderViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        placeOrderViewModel.methodRepo.showLoadingDialog(this@PlaceOrderActivity)
                    }
                    is ResponseSealed.Success -> {
                        when (event.response) {

                            is PlaceOrderResponse ->{
//                                cartViewModel.getCartItmes()
                                PlaceOrderDialog(this@PlaceOrderActivity,true,event.response.data){
                                    startActivity(
                                        Intent(this@PlaceOrderActivity,
                                            MainActivity::class.java)
                                    )
                                    finishAffinity()
                                }.show(supportFragmentManager,"Place")
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        if (event.message!!.code == 403) {
                            forcelogout(placeOrderViewModel.methodRepo)
                        }
                        placeOrderViewModel.methodRepo.hideLoadingDialog()
                    }
                    is ResponseSealed.Empty -> {
                        placeOrderViewModel.methodRepo.hideLoadingDialog()
                    }
                }
            }
        }
    }

    fun forcelogout(methodRepo: MethodsRepo){
        CommonDialogsUtils.showCommonDialog(this,methodRepo, "Log Out ",
            "Session Expire", false, false, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    lifecycleScope.launchWhenCreated {
                        methodRepo.dataStore.logOut()
                        startActivity(Intent(this@PlaceOrderActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
                override fun onCancel() {
                }
            })
    }
}