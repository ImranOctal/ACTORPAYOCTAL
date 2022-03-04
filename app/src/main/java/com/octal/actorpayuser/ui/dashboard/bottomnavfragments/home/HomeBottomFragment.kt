package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.MainActivity
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentHomeBottomBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletBalance
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletHistoryResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletListData
import com.octal.actorpayuser.ui.dashboard.adapters.AdapterWalletStatement
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class HomeBottomFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBottomBinding
    private val homeViewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_bottom, container, false)
        val root: View = binding.root

        apiResponse()
        homeViewModel.getWalletBalance()

        binding.rvtransactionID.apply {
            adapter = AdapterWalletStatement(requireContext(),homeViewModel.walletListData.items,homeViewModel.methodRepo){
                val bundle= bundleOf("item" to homeViewModel.walletListData.items[it])
                Navigation.findNavController(requireView()).navigate(R.id.action_homeBottomFragment_to_walletUserFragment,bundle)
            }
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        binding.homeViewAll.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.walletBottomFragment)
        }

        return root
    }

    fun updateUI(walletListData: WalletListData){

        homeViewModel.walletListData.items.clear()
        homeViewModel.walletListData.items.addAll(walletListData.items)


        binding.rvtransactionID.adapter?.notifyDataSetChanged()

        if(homeViewModel.walletListData.items.size>0) {
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
            homeViewModel.responseLive.collect { event ->
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
                            is WalletBalance -> {
                                (requireActivity() as MainActivity).updateBalnce(event.response.data.amount)
                                homeViewModel.walletListData.pageNumber=0
                                homeViewModel.getWalletHistory()
                            }
                        }
                        homeViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        homeViewModel.responseLive.value = ResponseSealed.Empty
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



}