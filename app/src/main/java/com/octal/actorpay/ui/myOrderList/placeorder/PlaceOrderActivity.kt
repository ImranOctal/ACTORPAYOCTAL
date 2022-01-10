package com.octal.actorpay.ui.myOrderList.placeorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.ActivityPlaceOrderBinding
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderParamas
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListData
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.ui.shippingaddress.details.ShippingAddressDetailsActivity
import com.octal.actorpay.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class PlaceOrderActivity : AppCompatActivity() {
    val placeOrderViewModel: PlaceOrderViewModel by inject()
    private lateinit var binding: ActivityPlaceOrderBinding

    companion object{
        var total=0.0
        var subTotal=0.0
        var gst=0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_order)
        apiResponse()
        cartResponse()
        setAdapter()

        placeOrderViewModel.getAddresses()

        binding.checkout.text="Pay ₹$total"
        binding.total.text="₹$total"
        binding.subTotal.text="₹$subTotal"
        binding.gst.text="₹$gst"

        binding.checkout.setOnClickListener {
            validate()
        }
        binding.addNewAddress.setOnClickListener {
            val intent = Intent(this, ShippingAddressDetailsActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            placeOrderViewModel.getAddresses()
        }


    fun setAdapter() {
        val adapter = CheckoutShippingListAdapter(
            this,
            placeOrderViewModel.shippingAddressList
        ) { position, action ->
            if(action.equals("Root")){
                placeOrderViewModel.shippingAddressList.onEach {
                    it.isSelect=false
                }
                placeOrderViewModel.shippingAddressList[position].isSelect=true
                binding.addressRecyclerview.adapter?.notifyDataSetChanged()
            }
            if (action.equals("Edit")) {
                ShippingAddressDetailsActivity.shippingAddressItem =
                    placeOrderViewModel.shippingAddressList.get(position)
                val intent = Intent(this, ShippingAddressDetailsActivity::class.java)
                resultLauncher.launch(intent)
            }
            else if(action.equals("Delete")){
                val shippingDeleteParams= ShippingDeleteParams(mutableListOf(placeOrderViewModel.shippingAddressList[position].id!!))
                placeOrderViewModel.deleteAddress(shippingDeleteParams)
            }
        }
        binding.addressRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.addressRecyclerview.adapter = adapter
    }

        fun validate(){
            val shippingAddressItem=placeOrderViewModel.shippingAddressList.find { it.isSelect==true }
            if(shippingAddressItem!=null) {
                val add_line_1 = shippingAddressItem.addressLine1
                var add_line_2 = ""
                if(shippingAddressItem.addressLine2!=null)
                 add_line_2 = shippingAddressItem.addressLine2!!
                val zipcode = shippingAddressItem.zipCode
                val city = shippingAddressItem.city
                val state = shippingAddressItem.state
                val country = shippingAddressItem.country
                val p_contact = shippingAddressItem.primaryContactNumber
                val s_contact = shippingAddressItem.secondaryContactNumber
                var isValid = true
                if (add_line_1.equals("") || zipcode.length < 6 || city.equals("") || state.equals("") || country.equals("") || p_contact.length < 5 || s_contact.length < 5) {
                    Toast.makeText(this,"Please Update Valid Address",Toast.LENGTH_SHORT).show()
                }else
                    placeOrderViewModel.placeOrder(
                        PlaceOrderParamas(
                            add_line_1,
                            add_line_2,
                            zipcode,
                            city,
                            state,
                            country,
                            p_contact,
                            s_contact
                        )
                    )
            }
            else{
                Toast.makeText(this,"Please Select Valid Address",Toast.LENGTH_SHORT).show()
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
                                PlaceOrderDialog(this@PlaceOrderActivity,placeOrderViewModel.methodRepo,true,event.response.data){
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

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            placeOrderViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        placeOrderViewModel.methodRepo.showLoadingDialog(this@PlaceOrderActivity)
                    }
                    is ResponseSealed.Success -> {
                        placeOrderViewModel.methodRepo.hideLoadingDialog()
                        when (event.response) {
                            is ShippingAddressListResponse -> {
                                updateUI(event.response.data)
                            }
                            is SuccessResponse -> {
                                placeOrderViewModel.getAddresses()
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        if (event.message!!.code == 403) {
                            forcelogout(placeOrderViewModel.methodRepo)
                        }
                        placeOrderViewModel.methodRepo.hideLoadingDialog()
                        Toast.makeText(this@PlaceOrderActivity,event.message.message,Toast.LENGTH_SHORT).show()
                    }
                    is ResponseSealed.Empty -> {
                        placeOrderViewModel.methodRepo.hideLoadingDialog()

                    }
                }
            }
        }


    }

    fun updateUI(addressListData: ShippingAddressListData) {
        placeOrderViewModel.shippingAddressList.clear()
        placeOrderViewModel.shippingAddressList.addAll(addressListData.items)
        placeOrderViewModel.shippingAddressList.onEachIndexed { index, shippingAddressItem ->
            shippingAddressItem.isSelect = index==0
        }
        binding.addressRecyclerview.adapter?.notifyDataSetChanged()
    }

    fun forcelogout(methodRepo: MethodsRepo){
        CommonDialogsUtils.showCommonDialog(this,methodRepo, "Log Out ",
            "Session Expire", false, false, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    lifecycleScope.launchWhenCreated {
                        methodRepo.dataStore.logOut()
                        methodRepo.dataStore.setIsIntro(true)
                        startActivity(Intent(this@PlaceOrderActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
                override fun onCancel() {
                }
            })
    }
}