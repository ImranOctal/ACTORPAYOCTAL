package com.octal.actorpay.ui.remittance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentRaiseConcernsBinding
import com.octal.actorpay.databinding.FragmentRemittanceBinding
import com.octal.actorpay.ui.productList.ProductsListFragment
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

class RemittanceFragment : BaseFragment() {
    private var _binding: FragmentRemittanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun WorkStation() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRemittanceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
        WorkStation()
        return root
    }

    companion object {
        private var instance: RemittanceFragment? = null
        @JvmStatic
        fun newInstance(): RemittanceFragment? {

            if (instance == null) {
                instance = RemittanceFragment()
            }
            return instance
        }
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