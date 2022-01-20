package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentWalletBottomBinding
import com.octal.actorpay.ui.dashboard.adapters.AdapterWalletStatement

class WalletBottomFragment : BaseFragment() {
    private lateinit var binding: FragmentWalletBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvItemsWalletID.apply {
            adapter = AdapterWalletStatement()
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }


        return root
    }

}