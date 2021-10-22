package com.octal.actorpay.ui.refer_and_earn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.R
import com.octal.actorpay.databinding.FragmentRaiseConcernsBinding
import com.octal.actorpay.databinding.FragmentReferAndEarnBinding
import com.octal.actorpay.databinding.FragmentRewardsPointsBinding
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class ReferAndEarnFragment : Fragment() {
    private val viewModel: ActorPayViewModel by  inject()
    private lateinit var binding:FragmentReferAndEarnBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_refer_and_earn, container, false)
        binding.toolbar.title.setText("My Order")
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


}