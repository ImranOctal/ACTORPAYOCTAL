package com.octal.actorpayuser.ui.raise_concern

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentRaiseConcernsBinding

class RaiseConcernFragment : BaseFragment() {
    private lateinit var binding: FragmentRaiseConcernsBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRaiseConcernsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()

        return root
    }


    fun init() {

    }
}