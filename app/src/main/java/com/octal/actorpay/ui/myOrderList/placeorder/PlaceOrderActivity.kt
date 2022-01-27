package com.octal.actorpay.ui.myOrderList.placeorder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.ActivityPlaceOrderBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderParams
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListData
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpay.ui.shippingaddress.details.ShippingAddressDetailsActivity
import com.octal.actorpay.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class PlaceOrderActivity : BaseActivity() {
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
//        cartResponse()
        setAdapter()

        placeOrderViewModel.getAddresses()

        binding.checkout.text="Pay ₹$total"
        binding.total.text="₹$total"
        binding.subTotal.text="₹$subTotal"
        binding.gst.text="₹$gst"

        binding.back.setOnClickListener {
            onBackPressed()
        }

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
            if(action == Clicks.Root){
                placeOrderViewModel.shippingAddressList.onEach {
                    it.isSelect=false
                }
                placeOrderViewModel.shippingAddressList[position].isSelect=true
                binding.addressRecyclerview.adapter?.notifyDataSetChanged()
            }
            if (action == Clicks.Edit) {

                val intent = Intent(this, ShippingAddressDetailsActivity::class.java)
                intent.putExtra("shippingItem", placeOrderViewModel.shippingAddressList[position])
                resultLauncher.launch(intent)
            }
            else if(action == Clicks.Delete){
                val shippingDeleteParams= ShippingDeleteParams(mutableListOf(placeOrderViewModel.shippingAddressList[position].id!!))
                CommonDialogsUtils.showCommonDialog(this,placeOrderViewModel.methodRepo,
                    "Delete Address","Are you sure?",
                    autoCancelable = true,
                    isCancelAvailable = true,
                    isOKAvailable = true,
                    showClickable = false,
                    callback = object :CommonDialogsUtils.DialogClick{
                        override fun onClick() {
                            placeOrderViewModel.deleteAddress(shippingDeleteParams)
                        }

                        override fun onCancel() {

                        }
                    })

            }
        }
        binding.addressRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.addressRecyclerview.adapter = adapter
    }

        fun validate(){
            val shippingAddressItem=placeOrderViewModel.shippingAddressList.find { it.isSelect==true }
            if(shippingAddressItem!=null) {
                val addLine1 = shippingAddressItem.addressLine1
                var addLine2 = ""
                if(shippingAddressItem.addressLine2!=null)
                    addLine2 = shippingAddressItem.addressLine2!!
                val zipcode = shippingAddressItem.zipCode
                val city = shippingAddressItem.city
                val state = shippingAddressItem.state
                val country = shippingAddressItem.country
                val pContact = shippingAddressItem.primaryContactNumber
                val sContact = shippingAddressItem.secondaryContactNumber
                if (addLine1 == "" || zipcode.length < 6 || city == "" || state == "" || country == "" || pContact.length < 5 || sContact.length < 5) {
                    Toast.makeText(this,"Please Update Valid Address",Toast.LENGTH_SHORT).show()
                }else
                    placeOrderViewModel.placeOrder(
                        PlaceOrderParams(
                            addLine1,
                            addLine2,
                            zipcode,
                            city,
                            state,
                            country,
                            pContact,
                            sContact
                        )
                    )
            }
            else{
                Toast.makeText(this,"Please Select Valid Address",Toast.LENGTH_SHORT).show()
            }
    }


    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            placeOrderViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        hideLoadingDialog()
                        when (event.response) {
                            is ShippingAddressListResponse -> {
                                updateUI(event.response.data)
                            }
                            is SuccessResponse -> {
                                placeOrderViewModel.getAddresses()
                            }
                            is PlaceOrderResponse ->{
                                PlaceOrderDialog(this@PlaceOrderActivity,placeOrderViewModel.methodRepo,event.response.data){
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

    private fun updateUI(addressListData: ShippingAddressListData) {
        placeOrderViewModel.shippingAddressList.clear()
        placeOrderViewModel.shippingAddressList.addAll(addressListData.items)
        placeOrderViewModel.shippingAddressList.onEachIndexed { index, shippingAddressItem ->
            shippingAddressItem.isSelect = index==0
        }
        binding.addressRecyclerview.adapter?.notifyDataSetChanged()
    }
}