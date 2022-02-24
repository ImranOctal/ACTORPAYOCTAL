package com.octal.actorpayuser.ui.transferMoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentPayBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_EMAIL
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_KEY
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_MOBILE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_NAME
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_QR
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.TransferMoneyParams
import com.octal.actorpayuser.utils.TransactionStatusSuccessDialog
import kotlinx.coroutines.flow.collect
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
        apiResponse()

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
                beneficiaryContact.setText(name)
            } else {
                cardContact.visibility = View.VISIBLE
                beneficiaryName.text = "Pay"
                beneficiaryContact.setText(getString(R.string.enter_mobile_no_or_email))
                payNow.visibility=View.GONE
            }

//            payNow.setOnClickListener {
//                DummyTransactionProcessDialog(requireActivity(),transferMoneyViewModel.methodRepo){
//                        action ->
//                    when(action){
//                        Clicks.Success->{
//                            binding.beneficiaryAmount.setText("")
//                            binding.beneficiaryReason.setText("")
//                            DummyTransactionStatusDialog(requireActivity(),transferMoneyViewModel.methodRepo,true).show(childFragmentManager,"status")
//                        }
//                        Clicks.Cancel->{
//                            DummyTransactionStatusDialog(requireActivity(),transferMoneyViewModel.methodRepo,false).show(childFragmentManager,"status")
//                        }
//                        else ->Unit
//                    }
//                }.show(childFragmentManager,"process")
//            }

            payNow.setOnClickListener {
                var isValidate=true
                val amount = binding.beneficiaryAmount.text.toString().trim()
                val reason = binding.beneficiaryReason.text.toString().trim()
                if (amount.equals("")) {
                    binding.beneficiaryAmount.error = "Please Enter Amount"
                    binding.beneficiaryAmount.requestFocus()
                    isValidate=false
                } else if (amount.toDouble() < 1) {
                    binding.beneficiaryAmount.error = "Amount should not less 1"
                    binding.beneficiaryAmount.requestFocus()
                    isValidate=false
                }
                if(reason == ""){
                    binding.beneficiaryReason.error = "Please Enter Reason"
                    isValidate=false
                }
                if(isValidate)
                {
                    transferMoneyViewModel.transferMoney(TransferMoneyParams(name,amount,reason))
                }
            }
        }




        return binding.root
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            transferMoneyViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is SuccessResponse -> {
                                TransactionStatusSuccessDialog(
                                    requireActivity(),
                                    transferMoneyViewModel.methodRepo,
                                    binding.beneficiaryAmount.text.toString().toDouble(),
                                    "Amount ₹${binding.beneficiaryAmount.text} has been added to\n" +
                                            "${name} wallet successfully"
                                ) {
                                    val navOptions = NavOptions.Builder()
                                        .setPopUpTo(R.id.homeBottomFragment, false).build()
                                    Navigation.findNavController(requireView())
                                        .navigate(R.id.walletBottomFragment, null, navOptions)
                                }.show(childFragmentManager, "status")

                                binding.beneficiaryAmount.setText("")
                                binding.beneficiaryReason.setText("")
                            }
                        }
                        transferMoneyViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        transferMoneyViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(transferMoneyViewModel.methodRepo)
                        }
                        else{
                            showCustomToast("Something went wrong, Please check beneficiary contact")
                        }
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }
    }
}