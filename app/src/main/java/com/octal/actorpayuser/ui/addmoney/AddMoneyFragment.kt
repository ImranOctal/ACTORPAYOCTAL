package com.octal.actorpayuser.ui.addmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.actorpay.merchant.utils.SingleClickListener
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentAddMoneyBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.AddMoneyParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.AddMoneyResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletBalance
import com.octal.actorpayuser.utils.TransactionStatusSuccessDialog
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class AddMoneyFragment : BaseFragment() {

    lateinit var binding: FragmentAddMoneyBinding
    private val addMoneyViewModel: AddMoneyViewModel by inject()

    var startAmount=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startAmount = it.getString(AppConstance.KEY_AMOUNT)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMoneyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addMoneyViewModel.getWalletBalance()
        apiResponse()


        addAmount(startAmount)


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

    fun addAmount(amount: String) {
        binding.enterAmountEdt.error = null
        binding.enterAmountEdt.setText(amount)
        binding.enterAmountEdt.setSelection(binding.enterAmountEdt.text.toString().length)
    }

    fun validate() {
        val amount = binding.enterAmountEdt.text.toString().trim()
        if (amount.equals("")) {
            binding.enterAmountEdt.error = "Please Enter Amount"
            binding.enterAmountEdt.requestFocus()
        } else if (amount.toDouble() < 1) {
            binding.enterAmountEdt.error = "Amount should not less 1"
            binding.enterAmountEdt.requestFocus()
        } else {
            lifecycleScope.launchWhenCreated {
                addMoneyViewModel.methodRepo.dataStore.getEmail().collect {
                    email->
            addMoneyViewModel.addMoney(AddMoneyParams(amount,email))
                }
            }
        }
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            addMoneyViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is AddMoneyResponse -> {
                                TransactionStatusSuccessDialog(
                                    requireActivity(),
                                    addMoneyViewModel.methodRepo,
                                    "Amount ₹${binding.enterAmountEdt.text}\n"+
                                            "added into wallet successfully",
                                    event.response.data
                                ) {
                                    when(it){
                                        Clicks.DONE ->{
                                            val navOptions = NavOptions.Builder()
                                                .setPopUpTo(R.id.homeBottomFragment, false).build()
                                            Navigation.findNavController(requireView())
                                                .navigate(R.id.walletBottomFragment, null, navOptions)
                                        }
                                        Clicks.BACK ->{
                                            addMoneyViewModel.getWalletBalance()
                                        }
                                        Clicks.Root->{
                                            val navOptions = NavOptions.Builder()
                                                .setPopUpTo(R.id.homeBottomFragment, true).build()
                                            Navigation.findNavController(requireView())
                                                .navigate(R.id.homeBottomFragment, null, navOptions)
                                        }
                                        else->Unit
                                    }

                                }.show(childFragmentManager, "status")

                                binding.enterAmountEdt.setText("")
                            }
                            is WalletBalance ->{
                                binding.tvAmount.text= "₹ "+event.response.data.amount.toString()
                            }
                        }
                        addMoneyViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        addMoneyViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(addMoneyViewModel.methodRepo)
                        }
                        else
                            showCustomToast(event.message.message)

                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }
    }
}