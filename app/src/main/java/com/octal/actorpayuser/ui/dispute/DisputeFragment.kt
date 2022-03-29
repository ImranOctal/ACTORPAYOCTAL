package com.octal.actorpayuser.ui.dispute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentDisputeBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.DisputeListData
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.DisputeListResponse
import com.octal.actorpayuser.utils.EndlessRecyclerViewScrollListener
import com.octal.actorpayuser.utils.OnFilterClick
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class DisputeFragment : BaseFragment(), OnFilterClick {

    private lateinit var binding: FragmentDisputeBinding
    private val disputeViewModel: DisputeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dispute, container, false)

        onFilterClick(this)
        apiResponse()
        disputeViewModel.getAllDisputes()
        setAdapter()




        return binding.root
    }


    private fun setAdapter() {
        val adapter = DisputeAdapter(
            disputeViewModel.disputeListData.items,
            disputeViewModel.methodRepo
        ) { action, position ->
            when (action) {
                Clicks.Root -> {
                    val bundle = bundleOf(
                        "disputeId" to disputeViewModel.disputeListData.items[position].disputeId,
                        "disputeCode" to disputeViewModel.disputeListData.items[position].disputeCode
                    )
                    Navigation.findNavController(requireView())
                        .navigate(R.id.disputeDetailsFragment, bundle)
                }
                Clicks.Success -> {

                    val bundle =
                        bundleOf("orderNo" to disputeViewModel.disputeListData.items[position].orderNo)
                    Navigation.findNavController(requireView())
                        .navigate(R.id.orderDetailsFragment, bundle, null)
                }
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.rvDispute.layoutManager = layoutManager
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (disputeViewModel.disputeListData.pageNumber < disputeViewModel.disputeListData.totalPages - 1) {
                        disputeViewModel.disputeListData.pageNumber += 1
                        disputeViewModel.getAllDisputes()
                    }
                }
            }
        binding.rvDispute.addOnScrollListener(endlessRecyclerViewScrollListener)
        binding.rvDispute.adapter = adapter
    }

    fun updateUI(disputeListData: DisputeListData) {
        disputeViewModel.disputeListData.pageNumber =
            disputeListData.pageNumber
        disputeViewModel.disputeListData.totalPages =
            disputeListData.totalPages
        disputeViewModel.disputeListData.items.clear()
        disputeViewModel.disputeListData.items.addAll(disputeListData.items)
        binding.rvDispute.adapter?.notifyDataSetChanged()

        if (disputeViewModel.disputeListData.items.size > 0) {
            binding.imageEmpty.visibility = View.GONE
            binding.textEmpty.visibility = View.GONE
        } else {
            binding.imageEmpty.visibility = View.VISIBLE
            binding.textEmpty.visibility = View.VISIBLE
        }
    }


    fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            disputeViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is DisputeListResponse -> {
                                updateUI(event.response.data)
                            }
                        }
                        disputeViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        disputeViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(disputeViewModel.methodRepo)
                        }
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun onClick() {
        DisputeFilterDialog(
            disputeViewModel.disputeListParams,
            requireActivity(),
            disputeViewModel.methodRepo
        ) {
            disputeViewModel.disputeListParams = it
            disputeViewModel.disputeListData.pageNumber = 0
            disputeViewModel.disputeListData.totalPages = 0
            disputeViewModel.disputeListData.items.clear()
            disputeViewModel.getAllDisputes()

        }.show()
    }
}