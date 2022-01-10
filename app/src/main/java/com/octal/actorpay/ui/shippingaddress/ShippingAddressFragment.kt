package com.octal.actorpay.ui.shippingaddress

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.FragmentShippingAddressBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListData
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpay.ui.cart.CartActivity
import com.octal.actorpay.ui.shippingaddress.details.ShippingAddressDetailsActivity
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class ShippingAddressFragment : BaseFragment() {

    private var _binding: FragmentShippingAddressBinding? = null
    private val binding get() = _binding!!

    private val shippingAddressViewModel: ShippingAddressViewModel by inject()
    lateinit var adapter: ShippingListAdapter

    override fun WorkStation() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShippingAddressBinding.inflate(inflater, container, false)

        setTitle("My Addresses")
        showHideBottomNav(false)
        showHideCartIcon(false)
        showHideFilterIcon(false)
        shippingAddressViewModel.getAddresses()
        setAdapter()
        apiResponse()

        binding.addAddress.setOnClickListener {
            /*startActivity(
                Intent(
                    requireContext(), ShippingAddressDetailsActivity::class.java
                )
            )  */
            val intent = Intent(requireContext(), ShippingAddressDetailsActivity::class.java)
            resultLauncher.launch(intent)

        }


        return binding.root
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            shippingAddressViewModel.getAddresses()
        }

    fun updateUI(addressListData: ShippingAddressListData) {
        shippingAddressViewModel.shippingAddressList.clear()
        shippingAddressViewModel.shippingAddressList.addAll(addressListData.items)
        shippingAddressViewModel.shippingAddressList.forEach {
            if (it.primary)
                it.isSelect = true
        }
        binding.shippingRecyclerview.adapter?.notifyDataSetChanged()
    }

    fun setAdapter() {
        adapter = ShippingListAdapter(
            requireContext(),
            shippingAddressViewModel.shippingAddressList
        ) { position, action ->
            if (action.equals("Edit")) {
                ShippingAddressDetailsActivity.shippingAddressItem =
                    shippingAddressViewModel.shippingAddressList.get(position)
                /*startActivity(
                    Intent(
                        requireContext(), ShippingAddressDetailsActivity::class.java
                    )
                )*/
                val intent = Intent(requireContext(), ShippingAddressDetailsActivity::class.java)
                resultLauncher.launch(intent)
            } else if (action.equals("Delete")) {
                val shippingDeleteParams =
                    ShippingDeleteParams(mutableListOf(shippingAddressViewModel.shippingAddressList[position].id!!))
                shippingAddressViewModel.deleteAddress(shippingDeleteParams)
            }
        }
        binding.shippingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.shippingRecyclerview.adapter = adapter
    }


    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            shippingAddressViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        shippingAddressViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ResponseSealed.Success -> {
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()
                        when (event.response) {
                            is ShippingAddressListResponse -> {
                                updateUI(event.response.data)
                            }
                            is SuccessResponse -> {
                                shippingAddressViewModel.getAddresses()
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        if (event.message!!.code == 403) {
                            forcelogout(shippingAddressViewModel.methodRepo)
                        }
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()
                        showCustomToast(event.message.message)
                    }
                    is ResponseSealed.Empty -> {
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()

                    }
                }
            }
        }


    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ShippingAddressFragment().apply {

            }
    }
}