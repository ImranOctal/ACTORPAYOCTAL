package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.databinding.FragmentHomeBottomBinding
import com.octal.actorpay.ui.adapter.TransactionAdapter
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class HomeBottomFragment : Fragment() {
    private var _binding: FragmentHomeBottomBinding? = null
    private val viewModel: ActorPayViewModel by  inject()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        gettransaction()
        return root
    }

    private fun gettransaction() {
        binding.rvtransactionID.apply {
            var arraylist: ArrayList<String> = arrayListOf("AddMoney", "AddMoney", "AddMoney")
            adapter = TransactionAdapter(arraylist, requireActivity())
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}