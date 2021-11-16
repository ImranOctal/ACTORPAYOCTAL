package com.octal.actorpay.ui.misc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.R
import com.octal.actorpay.databinding.FragmentMiscBinding
import com.octal.actorpay.ui.remittance.RemittanceFragment
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

class MiscFragment : Fragment() {
    private val viewModel: ActorPayViewModel by  inject()
    private lateinit var binding:FragmentMiscBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        private var instance: MiscFragment? = null
        @JvmStatic
        fun newInstance(): MiscFragment? {

            if (instance == null) {
                instance = MiscFragment()
            }
            return instance
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_misc, container, false)
        return binding.root

    }
}