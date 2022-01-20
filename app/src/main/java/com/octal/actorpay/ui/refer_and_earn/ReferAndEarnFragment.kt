package com.octal.actorpay.ui.refer_and_earn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentReferAndEarnBinding


class ReferAndEarnFragment : BaseFragment() {

    private lateinit var binding:FragmentReferAndEarnBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_and_earn, container, false)


        return binding.root
    }



}