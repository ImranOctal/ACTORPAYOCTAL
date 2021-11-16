package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentProfileBottomBinding
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

class ProfileBottomFragment : BaseFragment() {
    private var _binding: FragmentProfileBottomBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        private var instance: ProfileBottomFragment? = null
        @JvmStatic
        fun newInstance(): ProfileBottomFragment? {
            if (instance == null) {
                instance = ProfileBottomFragment()
            }
            return instance
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root
        WorkStation()
        return root
        // Inflate the layout for this fragment
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun WorkStation() {

    }
}