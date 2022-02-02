package com.octal.actorpay.ui.myOrderList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.FragmentMyOrderListBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListData
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListParams
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListResponse
import com.octal.actorpay.utils.EndlessRecyclerViewScrollListener
import com.octal.actorpay.utils.OnFilterClick
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class MyOrdersListFragment : BaseFragment(), OnFilterClick {

    private lateinit var binding: FragmentMyOrderListBinding
    private val orderViewModel: OrderViewModel by inject()
    lateinit var adapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//         orderViewModel.getAllOrders(orderViewModel.orderListParams)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_order_list, container, false)

        apiResponse()
        orderViewModel.orderListParams= OrderListParams()
        orderViewModel.orderListData.pageNumber = 0
        orderViewModel.orderListData.totalPages = 0
        orderViewModel.orderListData.items.clear()
        orderViewModel.getAllOrders(orderViewModel.orderListParams)

        setAdapter()
        onFilterClick(this)


        return binding.root
    }

    private fun setAdapter() {
        adapter = OrderListAdapter(
            orderViewModel.methodRepo,
            orderViewModel.orderListData.items
        ) { position, action ->
            if (action == Clicks.Details) {
//                val bundle = bundleOf("order" to orderViewModel.orderListData.items[position])
                val bundle =
                    bundleOf("orderNo" to orderViewModel.orderListData.items[position].orderNo)
                Navigation.findNavController(requireView())
                    .navigate(R.id.orderDetailsFragment, bundle)

            }
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewOrderList.layoutManager = layoutManager
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (orderViewModel.orderListData.pageNumber < orderViewModel.orderListData.totalPages - 1) {
                        orderViewModel.orderListData.pageNumber += 1
                        orderViewModel.getAllOrders(orderViewModel.orderListParams)
                    }
                }
            }
        binding.recyclerViewOrderList.addOnScrollListener(endlessRecyclerViewScrollListener)
        binding.recyclerViewOrderList.adapter = adapter
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            orderViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is OrderListResponse -> {
                                updateUI(event.response.data)
                            }
                        }
                        orderViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        orderViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(orderViewModel.methodRepo)
                        }
                        updateUI(orderViewModel.orderListData)
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }
    }

    private fun updateUI(orderListData: OrderListData) {
        orderViewModel.orderListData.pageNumber =
            orderListData.pageNumber
        orderViewModel.orderListData.totalPages =
            orderListData.totalPages
        orderViewModel.orderListData.items.addAll(orderListData.items)
        binding.recyclerViewOrderList.adapter?.notifyDataSetChanged()

        if (orderViewModel.orderListData.items.size > 0) {
            binding.imageEmpty.visibility = View.GONE
            binding.textEmpty.visibility = View.GONE
        } else {
            binding.imageEmpty.visibility = View.VISIBLE
            binding.textEmpty.visibility = View.VISIBLE
        }
    }


    override fun onClick() {
        OrderFilterDialog(
            orderViewModel.orderListParams,
            requireActivity(),
            orderViewModel.methodRepo
        ) {
            orderViewModel.orderListParams = it
            orderViewModel.orderListData.pageNumber = 0
            orderViewModel.orderListData.totalPages = 0
            orderViewModel.orderListData.items.clear()
            orderViewModel.getAllOrders(orderViewModel.orderListParams)

        }.show()
    }


}