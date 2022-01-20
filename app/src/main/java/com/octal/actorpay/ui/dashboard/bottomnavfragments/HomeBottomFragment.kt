package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentHomeBottomBinding
import com.octal.actorpay.ui.dashboard.adapters.TransactionAdapter


class HomeBottomFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBottomBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_bottom, container, false)
        val root: View = binding.root

        binding.rvtransactionID.apply {
            adapter = TransactionAdapter()
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }

        return root
    }



}