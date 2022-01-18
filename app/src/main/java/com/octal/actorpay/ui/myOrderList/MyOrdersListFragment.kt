
 package com.octal.actorpay.ui.myOrderList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.FragmentMyOrderListBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListData
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListParams
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListResponse
import com.octal.actorpay.ui.myOrderList.orderdetails.OrderDetailsFragment
import com.octal.actorpay.utils.OnFilterClick
import com.techno.taskmanagement.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


 class MyOrdersListFragment : BaseFragment(), OnFilterClick {

     private lateinit var binding: FragmentMyOrderListBinding
     private val orderViewModel: OrderViewModel by inject()
     lateinit var adapter: OrderListAdapter

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         apiResponse()
         orderViewModel.orderListParams= OrderListParams()
         orderViewModel.orderListData.pageNumber = 0
         orderViewModel.orderListData.totalPages = 0
         orderViewModel.orderListData.items.clear()
         orderViewModel.getAllOrders(orderViewModel.orderListParams)
     }

     companion object {
         private var instance: MyOrdersListFragment? = null

         @JvmStatic
         fun newInstance(): MyOrdersListFragment? {
             if (instance == null) {
                 instance = MyOrdersListFragment()
             }
             return instance
         }
     }

     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order_list, container, false)
         showHideBottomNav(false)
         showHideCartIcon(false)
         showHideFilterIcon(true)
         setAdapter()
         onFilterClick(this)


         return binding.root
     }

     private fun setAdapter(){
         adapter=OrderListAdapter(requireActivity(),orderViewModel.methodRepo,orderViewModel.orderListData.items,childFragmentManager){
             position, action ->
            if(action.equals(Clicks.Details)){
                 startFragment(
                     OrderDetailsFragment.newInstance(orderViewModel.orderListData.items[position]),
                     addToBackStack = true,
                     OrderDetailsFragment.toString()
                 )
             }
         }
         val layoutManager = LinearLayoutManager(requireContext())

         binding.recyclerViewOrderList.layoutManager=layoutManager
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
         binding.recyclerViewOrderList.adapter=adapter
     }

     fun apiResponse() {

         lifecycleScope.launchWhenStarted {
             orderViewModel.responseLive.collect { event ->
                 when (event) {
                     is ResponseSealed.loading -> {
                         orderViewModel.methodRepo.showLoadingDialog(requireContext())
                     }
                     is ResponseSealed.Success -> {
                         orderViewModel.methodRepo.hideLoadingDialog()
                         when (event.response) {
                             is OrderListResponse -> {
                                 updateUI(event.response.data)
                             }

                             is SuccessResponse -> {
                                orderViewModel.getAllOrders(orderViewModel.orderListParams)
                             }
                         }
                     }
                     is ResponseSealed.ErrorOnResponse -> {
                         if (event.message!!.code == 403) {
                             forcelogout(orderViewModel.methodRepo)
                         }

                         orderViewModel.methodRepo.hideLoadingDialog()
                         showCustomToast(event.message.message)
                         updateUI(orderViewModel.orderListData)
                     }
                     is ResponseSealed.Empty -> {
                         orderViewModel.methodRepo.hideLoadingDialog()

                     }
                 }
             }
         }
     }

     fun updateUI(orderListData: OrderListData){
         orderViewModel.orderListData.pageNumber =
             orderListData.pageNumber
         orderViewModel.orderListData.totalPages =
             orderListData.totalPages
         orderViewModel.orderListData.items.addAll(orderListData.items)
         binding.recyclerViewOrderList.adapter?.notifyDataSetChanged()

         if(orderViewModel.orderListData.items.size>0){
             binding.imageEmpty.visibility=View.GONE
             binding.textEmpty.visibility=View.GONE
         }
         else {
             binding.imageEmpty.visibility=View.VISIBLE
             binding.textEmpty.visibility=View.VISIBLE
         }
     }



     override fun onClick() {
         OrderFilterDialog(orderViewModel.orderListParams,requireActivity(),orderViewModel.methodRepo){
             orderViewModel.orderListParams=it
             orderViewModel.orderListData.pageNumber = 0
             orderViewModel.orderListData.totalPages = 0
             orderViewModel.orderListData.items.clear()
             orderViewModel.getAllOrders(orderViewModel.orderListParams)

         }.show()
     }

     override fun WorkStation() {

     }

 }