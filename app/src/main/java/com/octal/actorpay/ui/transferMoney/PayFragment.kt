package com.octal.actorpay.ui.transferMoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentPayBinding
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_EMAIL
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_KEY
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_MOBILE
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_NAME
import com.octal.actorpay.repositories.AppConstance.AppConstance.Companion.KEY_QR
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionProcessDialog
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionStatusDialog
import org.koin.android.ext.android.inject


class PayFragment : BaseFragment() {

    private lateinit var binding: FragmentPayBinding
    private val transferMoneyViewModel: TransferMoneyViewModel by inject()
    private var key = ""
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(KEY_KEY)!!
            name = it.getString(KEY_NAME)!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, container, false)

        binding.apply {

            if (key == KEY_QR) {
                cardContact.visibility = View.GONE
                beneficiaryName.text = "Pay $name"
            } else if (key == KEY_MOBILE) {
                cardContact.visibility = View.VISIBLE
                beneficiaryName.text = "Pay with Mobile Numnber"
                beneficiaryContact.setText(getString(R.string.enter_mobile_number))
            } else if (key == KEY_EMAIL) {
                cardContact.visibility = View.VISIBLE
                beneficiaryName.text = "Pay with Email"
                beneficiaryContact.setText(getString(R.string.enter_email))

            } else {
                cardContact.visibility = View.VISIBLE
                beneficiaryName.text = "Pay"
                beneficiaryContact.setText(getString(R.string.enter_mobile_no_or_email))
                payNow.visibility=View.GONE
            }

            payNow.setOnClickListener {
                DummyTransactionProcessDialog(requireActivity(),transferMoneyViewModel.methodRepo){
                        action ->
                    when(action){
                        Clicks.Success->{
                            binding.beneficiaryAmount.setText("")
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
        }




        return binding.root
    }
}