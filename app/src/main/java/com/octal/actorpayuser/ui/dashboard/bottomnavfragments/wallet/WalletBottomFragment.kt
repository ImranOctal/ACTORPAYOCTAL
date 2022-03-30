package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentWalletBottomBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletBalance
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletHistoryResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletListData
import com.octal.actorpayuser.ui.dashboard.adapters.AdapterWalletStatement
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.viewmodels.WalletBottomViewModel
import com.octal.actorpayuser.utils.EndlessRecyclerViewScrollListener
import com.octal.actorpayuser.utils.OnFilterClick
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class WalletBottomFragment : BaseFragment() , OnFilterClick {
    private lateinit var binding: FragmentWalletBottomBinding
    private val walletBottomViewModel: WalletBottomViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletBottomViewModel.getWalletBalance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        walletBottomViewModel.walletListData.pageNumber=0
//        walletBottomViewModel.walletListData.items.clear()
//        binding.rvItemsWalletID.adapter?.notifyDataSetChanged()

        apiResponse()

        onFilterClick(this)

        val mLayoutManager = LinearLayoutManager(requireContext())
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(mLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (walletBottomViewModel.walletListData.pageNumber < walletBottomViewModel.walletListData.totalPages - 1) {
                        walletBottomViewModel.walletListData.pageNumber += 1
                        walletBottomViewModel.getWalletHistory()
                    }
                }
            }

        binding.rvItemsWalletID.apply {


            adapter = AdapterWalletStatement(requireContext(),walletBottomViewModel.walletListData.items,walletBottomViewModel.methodRepo){

                val bundle= bundleOf("item" to walletBottomViewModel.walletListData.items[it])
                Navigation.findNavController(requireView()).navigate(R.id.walletDetailsFragment,bundle)

            }
            layoutManager = mLayoutManager
            addOnScrollListener(endlessRecyclerViewScrollListener)
        }

        return root
    }

    fun updateUI(walletListData: WalletListData){

        walletBottomViewModel.walletListData.pageNumber =
            walletListData.pageNumber
        walletBottomViewModel.walletListData.totalPages =
            walletListData.totalPages
        walletBottomViewModel.walletListData.items.addAll(walletListData.items)
//        walletBottomViewModel.walletListData.items.addAll(walletListData.items.filter {
//            it.purchaseType != "ADMIN_WALLET_COMMISSION"
//
//        }.toMutableList())

        binding.rvItemsWalletID.adapter?.notifyDataSetChanged()

        if(walletBottomViewModel.walletListData.items.size>0) {
            binding.imageEmpty.visibility=View.GONE
            binding.textEmpty.visibility=View.GONE
        }
        else{
           binding.imageEmpty.visibility=View.VISIBLE
           binding.textEmpty.visibility=View.VISIBLE
        }
    }

    fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            walletBottomViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        if(event.isLoading)
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is WalletHistoryResponse -> {
                                updateUI(event.response.data)
                            }
                            is WalletBalance ->{
                                binding.tvAmount.text= "₹ "+event.response.data.amount.toString()

                                walletBottomViewModel.walletListData.pageNumber=0
                                walletBottomViewModel.walletListData.items.clear()
                                walletBottomViewModel.getWalletHistory()
                            }
                        }
                        walletBottomViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(walletBottomViewModel.methodRepo)
                        }
                        walletBottomViewModel.responseLive.value = ResponseSealed.Empty

                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }
    }

    override fun onClick() {

        WalletFilterDialog(walletBottomViewModel.walletParams,requireActivity(),walletBottomViewModel.methodRepo){
            walletBottomViewModel.walletParams=it
            walletBottomViewModel.walletListData.pageNumber=0
            walletBottomViewModel.walletListData.totalPages=0
            walletBottomViewModel.walletListData.items.clear()
            binding.rvItemsWalletID.adapter?.notifyDataSetChanged()
            walletBottomViewModel.getWalletHistory()
        }.show()


    }
}