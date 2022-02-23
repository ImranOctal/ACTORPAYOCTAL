package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.AddTransactionStatusDialogBinding
import com.octal.actorpayuser.databinding.FragmentWalletDetailsBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WalletData


class WalletDetailsFragment : BaseFragment() {


    lateinit var binding: FragmentWalletDetailsBinding
    lateinit var walletItem:WalletData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_wallet_details,container,false)

        arguments.let {
            walletItem=requireArguments().getSerializable("item") as WalletData
        }

        if(walletItem!=null){


        }



        return binding.root
    }


}