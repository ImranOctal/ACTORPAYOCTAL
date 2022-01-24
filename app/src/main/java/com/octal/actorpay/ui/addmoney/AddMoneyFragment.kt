package com.octal.actorpay.ui.addmoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.actorpay.merchant.utils.SingleClickListener
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentAddMoneyBinding
import com.octal.actorpay.databinding.FragmentWalletwStatementBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionProcessDialog
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionStatusDialog
import com.octal.actorpay.ui.productList.ProductViewModel
import org.koin.android.ext.android.inject


class AddMoneyFragment : BaseFragment() {

    lateinit var binding:FragmentAddMoneyBinding
    private val addMoneyViewModel: AddMoneyViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMoneyBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.buttonAddMoney.setOnClickListener(object : SingleClickListener() {
            override fun performClick(v: View?) {
                validate()
            }
        })
        binding.amount50.setOnClickListener {
            addAmount("50")
        }
        binding.amount100.setOnClickListener {
            addAmount("100")
        }
        binding.amount200.setOnClickListener {
            addAmount("200")
        }
        binding.amount500.setOnClickListener {
            addAmount("500")
        }
        binding.amount1000.setOnClickListener {
            addAmount("1000")
        }
        binding.amount2000.setOnClickListener {
            addAmount("2000")
        }

        return root
    }

    fun addAmount(amount:String){
        binding.enterAmountEdt.error=null
        binding.enterAmountEdt.setText(amount)
        binding.enterAmountEdt.setSelection(binding.enterAmountEdt.text.toString().length)
    }

    fun validate(){
        val amount=binding.enterAmountEdt.text.toString().trim()
        if(amount.equals("")){
            binding.enterAmountEdt.error="Please Enter Amount"
            binding.enterAmountEdt.requestFocus()
        }
        else if(amount.toDouble()<10)
        {
            binding.enterAmountEdt.error="Amount should not less 10"
            binding.enterAmountEdt.requestFocus()
        }
        else{
            DummyTransactionProcessDialog(requireActivity(),addMoneyViewModel.methodRepo){
                action ->
                when(action){
                    Clicks.Success->{
                        binding.enterAmountEdt.setText("")
                        DummyTransactionStatusDialog(requireActivity(),addMoneyViewModel.methodRepo,true).show(childFragmentManager,"status")
                    }
                    Clicks.Cancel->{
                        DummyTransactionStatusDialog(requireActivity(),addMoneyViewModel.methodRepo,false).show(childFragmentManager,"status")
                    }
                    else ->Unit
                }
            }.show(childFragmentManager,"process")
        }
    }
}