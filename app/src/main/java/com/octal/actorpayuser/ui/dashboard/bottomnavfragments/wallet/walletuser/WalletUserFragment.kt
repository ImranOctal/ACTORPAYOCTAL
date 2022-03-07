package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.wallet.walletuser

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
import com.octal.actorpayuser.databinding.FragmentWalletUserBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletData
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletHistoryResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletListData
import com.octal.actorpayuser.ui.myOrderList.OrderFilterDialog
import com.octal.actorpayuser.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class WalletUserFragment : BaseFragment() {

    private lateinit var binding: FragmentWalletUserBinding
    private val walletUserViewModel: WalletUserViewModel by inject()
    var walletData: WalletData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       arguments?.let {
            walletData=it.getSerializable("item") as WalletData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletUserBinding.inflate(inflater, container, false)

        walletUserViewModel.walletListData.pageNumber=0
        walletUserViewModel.walletListData.items.clear()
        apiResponse()

        if(walletData != null)
        {
            walletUserViewModel.walletParams.toUser=walletData!!.toUser
        walletUserViewModel.getWalletHistory()
            binding.userName.text=walletData!!.toUserName.replace(", ","")
            binding.userEmail.text=walletData!!.toEmail
            binding.userMobile.text=walletData!!.toMobileNo


            binding.pay.setOnClickListener {
                val bundle =
                    bundleOf(AppConstance.KEY_KEY to AppConstance.KEY_EMAIL, AppConstance.KEY_CONTACT to walletData!!.toEmail, AppConstance.KEY_NAME to walletData!!.toUserName.replace(", ",""))
                Navigation.findNavController(requireView())
                    .navigate(R.id.payFragment, bundle)
            }
        }

        binding.request.setOnClickListener {
            RequestMoneyDialog(requireActivity(),walletUserViewModel.methodRepo){
                amount, reason ->

            }.show()
        }

        val mLayoutManager = LinearLayoutManager(requireContext())
        mLayoutManager.reverseLayout=true
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(mLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (walletUserViewModel.walletListData.pageNumber < walletUserViewModel.walletListData.totalPages - 1) {
                        walletUserViewModel.walletListData.pageNumber += 1
                        walletUserViewModel.getWalletHistory()
                    }
                }
            }

        binding.rvWalletUser.apply {
            adapter = WalletUserAdapter(requireContext(),walletUserViewModel.walletListData.items,walletUserViewModel.methodRepo){

                val bundle= bundleOf("item" to walletUserViewModel.walletListData.items[it])
                Navigation.findNavController(requireView()).navigate(R.id.walletDetailsFragment,bundle)

            }
            layoutManager = mLayoutManager
            addOnScrollListener(endlessRecyclerViewScrollListener)
        }


        return binding.root
    }


    fun updateUI(walletListData: WalletListData){

        walletUserViewModel.walletListData.pageNumber =
            walletListData.pageNumber
        walletUserViewModel.walletListData.totalPages =
            walletListData.totalPages
        walletUserViewModel.walletListData.items.addAll(walletListData.items)
        binding.rvWalletUser.adapter?.notifyDataSetChanged()

        if(walletUserViewModel.walletListData.pageNumber ==0 )
        binding.rvWalletUser.scrollToPosition(0)



    }


    fun apiResponse() {
        lifecycleScope.launchWhenStarted {
            walletUserViewModel.responseLive.collect { event ->
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

                        }
                        walletUserViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(walletUserViewModel.methodRepo)
                        }
                        walletUserViewModel.responseLive.value = ResponseSealed.Empty

                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }
    }

}