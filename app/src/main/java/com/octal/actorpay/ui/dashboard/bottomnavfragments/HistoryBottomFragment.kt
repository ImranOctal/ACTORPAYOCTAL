package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentHistoryBottomBinding
import com.octal.actorpay.ui.adapter.AdapterHistory
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class HistoryBottomFragment : BaseFragment() {
    private var _binding: FragmentHistoryBottomBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root
        showHideBottomNav(true)
        showHideCartIcon(true)
        showHideFilterIcon(false)
        // Inflate the layout for this fragment
        WorkStation()
        return root
    }

    companion object {
        private var instance: HistoryBottomFragment? = null


        @JvmStatic
        fun newInstance(): HistoryBottomFragment? {

            if (instance == null) {
                instance = HistoryBottomFragment()
            }
            return instance
        }
    }
    override fun WorkStation() {
        binding.rvItemsHistoryID.apply {
            var arraylist: ArrayList<String> = arrayListOf("AddMoney", "AddMoney", "AddMoney")
            adapter = AdapterHistory(arraylist, requireActivity())
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }
    }


    override fun toString(): String {
        return "HistoryBottomFragment()"
    }
}