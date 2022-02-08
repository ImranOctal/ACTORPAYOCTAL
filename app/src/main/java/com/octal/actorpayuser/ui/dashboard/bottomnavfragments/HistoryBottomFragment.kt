package com.octal.actorpayuser.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentHistoryBottomBinding
import com.octal.actorpayuser.ui.dashboard.adapters.AdapterHistory


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