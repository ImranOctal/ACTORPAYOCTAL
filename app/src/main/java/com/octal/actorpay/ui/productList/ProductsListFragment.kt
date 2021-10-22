package com.octal.actorpay.ui.productList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.octal.actorpay.R
import com.octal.actorpay.databinding.FragmentProductsListBinding
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class ProductsListFragment : Fragment() {
    private val viewModel: ActorPayViewModel by  inject()
    private lateinit var binding:FragmentProductsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_products_list, container, false)
        binding.toolbar.title.setText("Products")
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root

    }

}