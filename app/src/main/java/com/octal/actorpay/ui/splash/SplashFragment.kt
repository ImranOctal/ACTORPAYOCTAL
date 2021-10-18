package com.octal.actorpay.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.octal.actorpay.R
import com.octal.actorpay.databinding.FragmentSplashBinding
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorPayViewModel by  inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =DataBindingUtil.inflate(inflater, R.layout.fragment_splash,container,false)
           // FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root
        lifecycleScope.launch (Dispatchers.Main){
            delay(2000L)
            Navigation.findNavController(root).navigate(R.id.action_splashFragment_to_loginFragment)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}