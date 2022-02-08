package com.octal.actorpayuser.ui.remittance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentRemittanceBinding

class RemittanceFragment : BaseFragment() {
    private lateinit var binding: FragmentRemittanceBinding




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRemittanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        return root
    }

    fun init() {


        binding.apply {
            buttonNext.setOnClickListener {
                tvCircleOne.setBackgroundResource(R.drawable.unselected_circle)
                tvCircleTwo.setBackgroundResource(R.drawable.selected_circle)
                tvCircleThree.setBackgroundResource(R.drawable.unselected_circle)

                firstLayout.visibility=View.GONE
                secondLayout.visibility=View.VISIBLE
            }
            buttonNextTwo.setOnClickListener {

                tvCircleOne.setBackgroundResource(R.drawable.unselected_circle)
                tvCircleTwo.setBackgroundResource(R.drawable.unselected_circle)
                tvCircleThree.setBackgroundResource(R.drawable.selected_circle)

                secondLayout.visibility=View.GONE
                thirdLayout.visibility=View.VISIBLE
            }
        }

    }
}