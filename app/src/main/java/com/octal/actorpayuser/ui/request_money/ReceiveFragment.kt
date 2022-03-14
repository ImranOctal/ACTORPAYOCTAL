package com.octal.actorpayuser.ui.request_money

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.octal.actorpayuser.databinding.FragmentReceiveBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.AddMoneyResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.RequestMoneyParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.RequestMoneyResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.TransferMoneyParams
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.utils.TransactionStatusSuccessDialog
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class ReceiveFragment : BaseFragment() {

    private val requestMoneyViewModel: RequestMoneyViewModel by inject()
    private lateinit var binding: FragmentReceiveBinding
    private var key = ""
    private var name = ""
    private var contact = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(AppConstance.KEY_KEY)!!
            contact = it.getString(AppConstance.KEY_CONTACT)!!
            name = it.getString(AppConstance.KEY_NAME)!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_receive, container, false)
        apiResponse()
        init()





        return binding.root
    }

    fun init() {
        binding.apply {

           if (key == AppConstance.KEY_MOBILE || key == AppConstance.KEY_EMAIL) {
                cardContact.visibility = View.VISIBLE
                beneficiaryName.text = "Request from $name"
                beneficiaryContact.setText(contact)
            }

            payNow.setOnClickListener {
                var isValidate=true
                val amount = binding.beneficiaryAmount.text.toString().trim()
                val reason = binding.beneficiaryReason.text.toString().trim()
                if (amount.equals("")) {
                    binding.beneficiaryAmount.error = "Please Enter Amount"
                    binding.beneficiaryAmount.requestFocus()
                    isValidate=false
                } else if (amount.toDouble() < 1) {
                    binding.beneficiaryAmount.error = "Amount should not less than 1"
                    binding.beneficiaryAmount.requestFocus()
                    isValidate=false
                }
                if(reason == ""){
                    binding.beneficiaryReason.error = "Please Enter Reason"
                    isValidate=false
                }
                if(isValidate)
                {
                    requestMoneyViewModel.requestMoney(RequestMoneyParams(contact,amount,reason))
                }
            }
        }
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            requestMoneyViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is RequestMoneyResponse->{
                                CommonDialogsUtils.showCommonDialog(requireActivity(),requestMoneyViewModel.methodRepo,
                                "Money Requested","Your money request has been sent",false,false,true,false,object :CommonDialogsUtils.DialogClick{
                                        override fun onClick() {
                                            Navigation.findNavController(requireView()).popBackStack(R.id.homeBottomFragment,false)
                                        }
                                        override fun onCancel() {

                                        }
                                    })
                            }
                        }
                        requestMoneyViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        requestMoneyViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(requestMoneyViewModel.methodRepo)
                        }
                        else{
//                            showCustomToast("Something went wrong, Please check beneficiary contact")
                            showCustomToast(event.message.message)
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