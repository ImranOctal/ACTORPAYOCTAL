package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.MainActivity
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentHomeBottomBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.AddMoneyResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletBalance
import com.octal.actorpayuser.ui.addmoney.AddMoneyViewModel
import com.octal.actorpayuser.ui.dashboard.adapters.TransactionAdapter
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
            adapter = TransactionAdapter()
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        return root
    }



    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            homeViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {

                            is WalletBalance -> {
                                (requireActivity() as MainActivity).updateBalnce(event.response.data.amount)
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