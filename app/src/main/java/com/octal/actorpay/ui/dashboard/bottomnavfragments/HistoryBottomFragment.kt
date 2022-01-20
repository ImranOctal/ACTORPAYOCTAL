package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentHistoryBottomBinding
import com.octal.actorpay.ui.dashboard.adapters.AdapterHistory


class HistoryBottomFragment : BaseFragment() {
    lateinit var binding: FragmentHistoryBottomBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.rvItemsHistoryID.apply {
            adapter = AdapterHistory()
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }
        return root
    }

}