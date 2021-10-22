package com.octal.actorpay.ui.walletStatement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.databinding.FragmentWalletwStatementBinding
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class WalletStatementFragment : Fragment() {
    private val viewModel: ActorPayViewModel by  inject()
    private var _binding: FragmentWalletwStatementBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWalletwStatementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.toolbar.title.setText("My Wallet")
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().popBackStack()

        }
        return root
    }

}