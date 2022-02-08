package com.octal.actorpay.ui.mobileanddth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentAddMoneyBinding
import com.octal.actorpay.databinding.FragmentMobileAndDTHBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.ui.addmoney.AddMoneyViewModel
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionProcessDialog
import com.octal.actorpay.ui.dummytransactionprocess.DummyTransactionStatusDialog
import org.koin.android.ext.android.inject


class MobileAndDTHFragment : BaseFragment() {


    lateinit var binding: FragmentMobileAndDTHBinding
    private val mobileAndDTHFragment: MobileAndDthViewModel by inject()
    val operatorList= mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMobileAndDTHBinding.inflate(inflater, container, false)
        val root: View = binding.root

        feedList(true)
        setAdapter()

        binding.radioMobile.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.radioDth.isChecked=false
                binding.enterNumberEdt.hint="Enter mobile number"
                binding.layoutType.visibility=View.VISIBLE
                feedList(true)
                setAdapter()
            }

        }
        binding.radioDth.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.radioMobile.isChecked=false
                binding.enterNumberEdt.hint="Enter DTH number"
                binding.layoutType.visibility=View.GONE
                feedList(false)
                setAdapter()
            }
        }

        binding.radioPrepaid.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.radioPostpaid.isChecked=false
            }
        }
        binding.radioPostpaid.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.radioPrepaid.isChecked=false
            }
        }

        binding.recharge.setOnClickListener {
            val amount=binding.enterAmountEdt.text.toString().trim()
            if(amount.equals("")){
                binding.enterAmountEdt.error="Please Enter Amount"
                binding.enterAmountEdt.requestFocus()
            }
            else if(amount.toDouble()<1)
            {
                binding.enterAmountEdt.error="Amount should not less 1"
                binding.enterAmountEdt.requestFocus()
            }
            else
            DummyTransactionProcessDialog(requireActivity(),mobileAndDTHFragment.methodRepo){
                    action ->
                when(action){
                    Clicks.Success->{
                        binding.enterAmountEdt.setText("")
                        DummyTransactionStatusDialog(requireActivity(),mobileAndDTHFragment.methodRepo,true).show(childFragmentManager,"status")
                    }
                    Clicks.Cancel->{
                        DummyTransactionStatusDialog(requireActivity(),mobileAndDTHFragment.methodRepo,false).show(childFragmentManager,"status")
                    }
                    else ->Unit
                }
            }.show(childFragmentManager,"process")
        }




        return root
    }

    fun setAdapter(){

        val adapter=OperatorAdapter(operatorList)

        binding.rvSelectOperator.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvSelectOperator.adapter=adapter
    }

    fun feedList(isMobile:Boolean){
        operatorList.clear()
        if(isMobile)
        {
            operatorList.add("Airtel")
            operatorList.add("JIO")
            operatorList.add("VI")
            operatorList.add("BSNL")
            operatorList.add("MTNL")
        }
        else
        {
            operatorList.add("Airtel")
            operatorList.add("JIO")
            operatorList.add("Vodaphone")
            operatorList.add("D2H")
            operatorList.add("MTNL")
        }

    }


}