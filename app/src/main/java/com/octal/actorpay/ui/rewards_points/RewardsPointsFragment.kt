package com.octal.actorpay.ui.rewards_points

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentRewardsPointsBinding
import com.octal.actorpay.ui.dashboard.bottomnavfragments.WalletBottomFragment
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class RewardsPointsFragment : BaseFragment() {
    private var _binding: FragmentRewardsPointsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRewardsPointsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        WorkStation()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private var instance: RewardsPointsFragment? = null
        @JvmStatic
        fun newInstance(): RewardsPointsFragment? {

            if (instance == null) {
                instance = RewardsPointsFragment()
            }
            return instance
        }
    }


    override fun WorkStation() {
    }

    override fun toString(): String {
        return "RewardsPointsFragment()"
    }


}