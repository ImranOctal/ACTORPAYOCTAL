package com.octal.actorpay.ui.transferMoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentTransferMoneyBinding
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_EMAIL
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_KEY
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_MOBILE
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_NAME
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_QR
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionProcessDialog
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionStatusDialog
import org.koin.android.ext.android.inject

class TransferMoneyFragment : BaseFragment() {
    private lateinit var binding:FragmentTransferMoneyBinding
    private val transferMoneyViewModel: TransferMoneyViewModel by inject()

    private var isWalletToWallet=true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_transfer_money, container, false)
        init()

        binding.scan.setOnClickListener {
            val bundle =
                bundleOf(KEY_KEY to KEY_QR, KEY_NAME to "John")
            Navigation.findNavController(requireView())
                .navigate(R.id.payFragment, bundle)
        }
        binding.emailNumberField.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validate()
                return@setOnEditorActionListener true;
            }
            return@setOnEditorActionListener false;

        }
        binding.payNow.setOnClickListener {
            DummyTransactionProcessDialog(requireActivity(),transferMoneyViewModel.methodRepo){
                    action ->
                when(action){
                    Clicks.Success->{
                        binding.beneficiaryName.setText("")
                        binding.beneficiaryAccountNo.setText("")
                        binding.beneficiaryIfsc.setText("")
                        binding.beneficiaryBranch.setText("")
                        binding.beneficiaryReason.setText("")
                        DummyTransactionStatusDialog(requireActivity(),transferMoneyViewModel.methodRepo,true).show(childFragmentManager,"status")
                    }
                    Clicks.Cancel->{
                        DummyTransactionStatusDialog(requireActivity(),transferMoneyViewModel.methodRepo,false).show(childFragmentManager,"status")
                    }
                    else ->Unit
                }
            }.show(childFragmentManager,"process")
        }

        return binding.root
    }

    fun init(){
        binding.walletToWalletBtn.setOnClickListener {
            if(!isWalletToWallet)
            {
                isWalletToWallet=true
                binding.walletToWalletBtn.setBackgroundResource(R.drawable.round_wallet_bg)
                binding.walletToBankBtn.setBackgroundResource(R.drawable.round_wallet_blue_bg)
                binding.layoutScanQr.visibility=View.VISIBLE
                binding.layoutBank.visibility=View.GONE
            }
        }
        binding.walletToBankBtn.setOnClickListener {
            if(isWalletToWallet)
            {
                isWalletToWallet=false
                binding.walletToWalletBtn.setBackgroundResource(R.drawable.round_wallet_blue_bg)
                binding.walletToBankBtn.setBackgroundResource(R.drawable.round_wallet_bg)
                binding.layoutScanQr.visibility=View.GONE
                binding.layoutBank.visibility=View.VISIBLE
            }
        }
    }

    fun validate(){
        transferMoneyViewModel.methodRepo.hideSoftKeypad(requireActivity())
        val contact=binding.emailNumberField.text.toString().trim()
        if(transferMoneyViewModel.methodRepo.isValidEmail(contact))
        {
            val bundle =
                bundleOf(KEY_KEY to KEY_EMAIL,KEY_NAME to "")
            Navigation.findNavController(requireView())
                .navigate(R.id.payFragment, bundle)
        }
        else if(transferMoneyViewModel.methodRepo.isValidPhoneNumber(contact))
        {
            val bundle =
                bundleOf(KEY_KEY to KEY_MOBILE, KEY_NAME to "")
            Navigation.findNavController(requireView())
                .navigate(R.id.payFragment, bundle)
        }
        else {
            binding.emailNumberField.error="Please enter valid input"
        }
    }

}