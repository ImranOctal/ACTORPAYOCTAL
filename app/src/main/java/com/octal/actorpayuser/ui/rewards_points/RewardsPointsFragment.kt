package com.octal.actorpayuser.ui.rewards_points

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentRewardsPointsBinding


class RewardsPointsFragment : BaseFragment() {
    private lateinit var binding: FragmentRewardsPointsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRewardsPointsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }



}