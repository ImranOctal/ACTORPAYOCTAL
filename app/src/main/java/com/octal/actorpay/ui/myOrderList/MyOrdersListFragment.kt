
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
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


 class MyOrdersListFragment : BaseFragment() {

     private lateinit var binding: FragmentMyOrderListBinding
     private val orderViewModel: OrderViewModel by inject()

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         apiResponse()
         orderViewModel.getAllOrders()
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
         showHideFilterIcon(false)
         setAdapter()


         return binding.root
     }

     private fun setAdapter(){
         val adapter=OrderListAdapter(orderViewModel.orderListData.items,childFragmentManager)
         binding.recyclerViewOrderList.layoutManager=LinearLayoutManager(requireContext())
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
//                                productViewModel.productData.pageNumber=event.response.data.pageNumber
                                 orderViewModel.orderListData.pageNumber =
                                     event.response.data.pageNumber
                                 orderViewModel.orderListData.totalPages =
                                     event.response.data.totalPages
                                 orderViewModel.orderListData.items.addAll(event.response.data.items)
                                 setAdapter()
//                                 adapter.notifyItemChanged(orderViewModel.orderListData.items.size - 1)
                             }
                         }
                     }
                     is ResponseSealed.ErrorOnResponse -> {
                         if (event.message!!.code == 403) {
                             forcelogout(orderViewModel.methodRepo)
                         }

                         orderViewModel.methodRepo.hideLoadingDialog()
                     }
                     is ResponseSealed.Empty -> {
                         orderViewModel.methodRepo.hideLoadingDialog()

                     }
                 }
             }
         }

     }


     override fun WorkStation() {

     }
 }