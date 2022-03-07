package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.wallet

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentWalletDetailsBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletData
import com.octal.actorpayuser.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpayuser.ui.content.ContentActivity
import com.octal.actorpayuser.ui.content.ContentViewModel
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.viewmodels.WalletBottomViewModel
import org.koin.android.ext.android.inject


class WalletDetailsFragment : BaseFragment() {


    lateinit var binding: FragmentWalletDetailsBinding
    lateinit var walletItem:WalletData
    private val walletBottomViewModel: WalletBottomViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_wallet_details,container,false)

        arguments.let {
            walletItem=requireArguments().getSerializable("item") as WalletData
        }

        if(walletItem!=null){

            binding.rowWalletText.text=walletItem.transactionRemark.replace(","," ")
            binding.rowWalletTxn.text=walletItem.walletTransactionId
            binding.rowWalletDate.text=walletBottomViewModel.methodRepo.getFormattedOrderDate(walletItem.createdAt)
            binding.rowWalletAmountDebit.text="₹ "+walletItem.transactionAmount.toString()
            binding.rowWalletName.text=walletItem.toUserName.replace(" ,","")

            walletBottomViewModel.methodRepo.makeTextLink(binding.rowWalletName,"${walletItem.toUserName.replace(", ","")}",true,ContextCompat.getColor(requireContext(),R.color.primary)){

                val bundle= bundleOf("item" to walletItem)
                Navigation.findNavController(requireView()).navigate(R.id.walletUserFragment,bundle)

            }

            if(walletItem.purchaseType == "TRANSFER"){
                binding.rowWalletName.visibility=View.VISIBLE
                binding.rowWalletNameText.visibility=View.VISIBLE
//                binding.rowWalletText.text="Money Transferred Successfully"
            }
            else if(walletItem.purchaseType == "SHOPPING"){
//                binding.rowWalletText.text="Online Shopping"
            }

            binding.rowWalletName.setOnClickListener {

            }

            if(walletItem.transactionTypes == "DEBIT"){
                binding.rowWalletAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.pink_color))
                binding.rowWalletAmountDebit.setTextColor(ContextCompat.getColor(requireContext(), R.color.pink_color))
                binding.rowWalletAmountDebit.text="₹ "+walletItem.transactionAmount.toString()
            }
            if(walletItem.transactionTypes == "CREDIT"){
                binding.rowWalletAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_color))
                binding.rowWalletAmountDebit.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_color))
                binding.rowWalletAmountDebit.text="₹ "+walletItem.transactionAmount.toString()
            }

            if(walletItem.percentage > 0.0) {
                binding.rowWalletAmount.text="₹ "+walletItem.transactionAmount.toString()
                binding.transferAmount.text="₹ "+walletItem.transferAmount.toString()
                binding.adminAmount.text="₹ "+walletItem.adminCommission.toString()
            }
            else{
                binding.cardSecondLayout.visibility=View.GONE
                binding.rowWalletAmountDebit.visibility=View.VISIBLE
                binding.rowWalletAmountDebitText.visibility=View.VISIBLE
            }

        }

        binding.btnDone.setOnClickListener {
            requireActivity().onBackPressed()
        }



        return binding.root
    }


}