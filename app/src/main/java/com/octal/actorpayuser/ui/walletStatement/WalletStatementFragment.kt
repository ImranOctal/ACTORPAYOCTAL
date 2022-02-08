package com.octal.actorpayuser.ui.walletStatement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentWalletwStatementBinding

class WalletStatementFragment : BaseFragment() {
    private var _binding: FragmentWalletwStatementBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentWalletwStatementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.toolbar.backIcon.setOnClickListener {
            findNavController().popBackStack()

        }
        return root
    }

}