package com.octal.actorpayuser.ui.shippingaddress

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentShippingAddressBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressListData
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingDeleteParams
import com.octal.actorpayuser.ui.shippingaddress.details.ShippingAddressDetailsActivity
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class ShippingAddressFragment : BaseFragment() {

    lateinit var binding: FragmentShippingAddressBinding

    private val shippingAddressViewModel: ShippingAddressViewModel by inject()
    lateinit var adapter: ShippingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShippingAddressBinding.inflate(inflater, container, false)


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

    private fun updateUI(addressListData: ShippingAddressListData) {
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
            if (action == "Edit") {

                /*startActivity(
                    Intent(
                        requireContext(), ShippingAddressDetailsActivity::class.java
                    )
                )*/
                val intent = Intent(requireContext(), ShippingAddressDetailsActivity::class.java)
                intent.putExtra("shippingItem",
                    shippingAddressViewModel.shippingAddressList[position]
                )
                resultLauncher.launch(intent)
            } else if (action == "Delete") {
                val shippingDeleteParams =
                    ShippingDeleteParams(mutableListOf(shippingAddressViewModel.shippingAddressList[position].id!!))
                CommonDialogsUtils.showCommonDialog(requireActivity(),shippingAddressViewModel.methodRepo,
                "Delete Address","Are you sure?",
                    autoCancelable = true,
                    isCancelAvailable = true,
                    isOKAvailable = true,
                    showClickable = false,
                    callback = object :CommonDialogsUtils.DialogClick{
                        override fun onClick() {
                            shippingAddressViewModel.deleteAddress(shippingDeleteParams)
                        }

                        override fun onCancel() {

                        }
                    })

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
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
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
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(shippingAddressViewModel.methodRepo)
                        }
                        else
                        showCustomToast(event.message.message)
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }


    }
}