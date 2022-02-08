package com.octal.actorpayuser.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentWalletBottomBinding
import com.octal.actorpayuser.ui.dashboard.adapters.AdapterWalletStatement

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