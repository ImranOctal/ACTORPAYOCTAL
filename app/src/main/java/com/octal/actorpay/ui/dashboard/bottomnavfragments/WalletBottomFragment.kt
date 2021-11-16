package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentWalletBottomBinding
import com.octal.actorpay.ui.adapter.AdapterWalletStatement
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

class WalletBottomFragment : BaseFragment() {
    private var _binding: FragmentWalletBottomBinding? = null
    private val binding get() = _binding!!


    companion object {
        private var instance: WalletBottomFragment? = null


        @JvmStatic
        fun newInstance(): WalletBottomFragment? {

            if (instance == null) {
                instance = WalletBottomFragment()
            }
            return instance
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root
        WorkStation()
        return root
    }

    override fun WorkStation() {
        binding.rvItemsWalletID.apply {
            var arraylist: ArrayList<String> = arrayListOf("AddMoney", "AddMoney", "AddMoney")
            adapter = AdapterWalletStatement(arraylist, requireActivity())
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }
    }

    override fun toString(): String {
        return "WalletBottomFragment()"
    }

}