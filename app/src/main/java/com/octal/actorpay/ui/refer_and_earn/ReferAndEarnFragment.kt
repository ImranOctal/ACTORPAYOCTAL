package com.octal.actorpay.ui.refer_and_earn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentRaiseConcernsBinding
import com.octal.actorpay.databinding.FragmentReferAndEarnBinding
import com.octal.actorpay.databinding.FragmentRewardsPointsBinding
import com.octal.actorpay.ui.rewards_points.RewardsPointsFragment
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class ReferAndEarnFragment : BaseFragment() {

    private lateinit var binding:FragmentReferAndEarnBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_and_earn, container, false)

        return binding.root
    }


    override fun WorkStation() {

    }
}