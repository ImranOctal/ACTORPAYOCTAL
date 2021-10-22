package com.octal.actorpay.ui.transferMoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.R
import com.octal.actorpay.databinding.FragmentTransferMoneyBinding
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

class TransferMoneyFragment : Fragment() {
    private val viewModel: ActorPayViewModel by  inject()
    private lateinit var binding:FragmentTransferMoneyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_transfer_money, container, false)
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}