package com.octal.actorpayuser.ui.myOrderList.placeorder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentPlaceOrderBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_AMOUNT
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.PlaceOrderParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.PlaceOrderResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressListData
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletBalance
import com.octal.actorpayuser.ui.cart.CartViewModel
import com.octal.actorpayuser.ui.shippingaddress.details.ShippingAddressDetailsActivity
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import java.text.DecimalFormat


class PlaceOrderFragment : BaseFragment() {

    lateinit var binding: FragmentPlaceOrderBinding
    private val cartViewModel: CartViewModel by inject()
    val placeOrderViewModel: PlaceOrderViewModel by inject()
    var total = 0.0
    var subTotal = 0.0
    var gst = 0.0
    var walletBalance=0.0

    var decimalFormat: DecimalFormat = DecimalFormat("0.00")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_order, container, false)
        placeOrderViewModel.getWalletBalance()
        apiResponse()
        setAdapter()

        if (arguments != null) {
            total = requireArguments().getDouble("total")
            subTotal = requireArguments().getDouble("subtotal")
            gst = requireArguments().getDouble("gst")
        }

        binding.checkout.text = "Pay ₹${total}"
        binding.total.text = "₹${total}"
        binding.subTotal.text = "₹${subTotal}"
        binding.gst.text = "₹${gst}"

        binding.checkout.setOnClickListener {
            validate()
        }
        binding.addNewAddress.setOnClickListener {
            val intent = Intent(requireContext(), ShippingAddressDetailsActivity::class.java)
            resultLauncher.launch(intent)
        }
        binding.addMoney.setOnClickListener {
//            val requireAmount=decimalFormat.format(total-walletBalance)
            val bundle= bundleOf(KEY_AMOUNT to "${Math.ceil(total-walletBalance)}")
            Navigation.findNavController(requireView()).navigate(R.id.addMoneyFragment,bundle)
        }

        return binding.root
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            placeOrderViewModel.getAddresses()
        }

    fun setAdapter() {
        val adapter = CheckoutShippingListAdapter(
            requireContext(),
            placeOrderViewModel.shippingAddressList
        ) { position, action ->
            if (action == Clicks.Root) {
                placeOrderViewModel.shippingAddressList.onEach {
                    it.isSelect = false
                }
                placeOrderViewModel.shippingAddressList[position].isSelect = true
                binding.addressRecyclerview.adapter?.notifyDataSetChanged()
            }
            if (action == Clicks.Edit) {
                val intent = Intent(requireActivity(), ShippingAddressDetailsActivity::class.java)
                intent.putExtra("shippingItem", placeOrderViewModel.shippingAddressList[position])
                resultLauncher.launch(intent)
            } else if (action == Clicks.Delete) {
                val shippingDeleteParams =
                    ShippingDeleteParams(mutableListOf(placeOrderViewModel.shippingAddressList[position].id!!))
                CommonDialogsUtils.showCommonDialog(requireActivity(),
                    placeOrderViewModel.methodRepo,
                    "Delete Address",
                    "Are you sure?",
                    autoCancelable = true,
                    isCancelAvailable = true,
                    isOKAvailable = true,
                    showClickable = false,
                    callback = object : CommonDialogsUtils.DialogClick {
                        override fun onClick() {
                            placeOrderViewModel.deleteAddress(shippingDeleteParams)
                        }

                        override fun onCancel() {

                        }
                    })

            }
        }
        binding.addressRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRecyclerview.adapter = adapter
    }

    fun validate() {
        val shippingAddressItem =
            placeOrderViewModel.shippingAddressList.find { it.isSelect == true }
        if (shippingAddressItem != null) {
            val addLine1 = shippingAddressItem.addressLine1
            var addLine2 = ""
            if (shippingAddressItem.addressLine2 != null)
                addLine2 = shippingAddressItem.addressLine2!!
            val zipcode = shippingAddressItem.zipCode
            val city = shippingAddressItem.city
            val state = shippingAddressItem.state
            val country = shippingAddressItem.country
            val pContact = shippingAddressItem.primaryContactNumber
            val sContact = shippingAddressItem.secondaryContactNumber

            if (binding.walletRadio.isChecked) {
                placeOrderViewModel.placeOrder(
                    PlaceOrderParams(
                        addLine1,
                        addLine2,
                        zipcode,
                        city,
                        state,
                        country,
                        shippingAddressItem.extensionNumber!!,
                        pContact,
                        sContact,
                        shippingAddressItem.latitude,
                        shippingAddressItem.longitude,
                        shippingAddressItem.name,
                        shippingAddressItem.area,
                        shippingAddressItem.title,
                    )
                )
            } else {
                showCustomToast("Please select payment option")
            }
        } else {
            showCustomToast("Please select valid address")
        }

    }


    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            placeOrderViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        placeOrderViewModel.responseLive.value = ResponseSealed.Empty
                        when (event.response) {
                            is ShippingAddressListResponse -> {
                                updateUI(event.response.data)
                            }
                            is SuccessResponse -> {
                                placeOrderViewModel.getAddresses()
                            }
                            is WalletBalance -> {
                                placeOrderViewModel.getAddresses()
                                walletBalance=event.response.data.amount
                                walletBalance=decimalFormat.format(walletBalance).toDouble()
                                updatePaymentGateway()
                            }
                            is PlaceOrderResponse -> {
                                PlaceOrderDialog(
                                    requireActivity(),
                                    placeOrderViewModel.methodRepo,
                                    event.response.data
                                ) {
                                    cartViewModel.cartItems.value.clear()
                                    if (it == "order") {
                                        val navOptions = NavOptions.Builder()
                                            .setPopUpTo(R.id.homeBottomFragment, false).build()
                                        Navigation.findNavController(requireView())
                                            .navigate(R.id.myOrderFragment, null, navOptions)
                                    }
                                    if (it == "shopping") {
                                        val navOptions = NavOptions.Builder()
                                            .setPopUpTo(R.id.homeBottomFragment, false).build()
                                        Navigation.findNavController(requireView())
                                            .navigate(R.id.productListFragment, null, navOptions)
                                    }
//                                    startActivity(
//                                        Intent(requireContext(),
//                                            MainActivity::class.java)
//                                    )
//                                    finishAffinity()
                                }.show(childFragmentManager, "Place")
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        placeOrderViewModel.responseLive.value = ResponseSealed.Empty
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

    private fun updatePaymentGateway() {
        if (total < walletBalance) {
            binding.walletRadio.isChecked = true
            binding.walletRadio.isEnabled = true
            binding.tvAmount.text = "₹$walletBalance"
            binding.addMoney.visibility = View.GONE
            binding.insufficientBalance.visibility=View.GONE
        } else {
            binding.walletRadio.isChecked = false
            binding.walletRadio.isClickable = false
            binding.walletRadio.isEnabled = false
            binding.tvAmount.text = "₹$walletBalance"
            binding.addMoney.visibility = View.GONE
            val requireAmount=Math.ceil(total-walletBalance)
            binding.addMoney.text = "Add ₹ ${requireAmount}"
            binding.insufficientBalance.visibility=View.VISIBLE
        }
    }


    private fun updateUI(addressListData: ShippingAddressListData) {
        placeOrderViewModel.shippingAddressList.clear()
        placeOrderViewModel.shippingAddressList.addAll(addressListData.items)
        placeOrderViewModel.shippingAddressList.onEachIndexed { index, shippingAddressItem ->
            shippingAddressItem.isSelect = index == 0
        }
        binding.addressRecyclerview.adapter?.notifyDataSetChanged()
    }

}