package com.octal.actorpayuser.ui.productList.productsfilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentProductFilterBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.SubCategorieResponse
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class ProductFilterFragment : BaseFragment() {

    private lateinit var binding: FragmentProductFilterBinding
    private val productFilterViewModel: ProductFilterViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productFilterViewModel.getCategories()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_filter, container, false)
//        setTitle("Filters")
//        showHideBottomNav(false)
//        showHideCartIcon(false)
//        showHideFilterIcon(false)


        return binding.root
    }

    private fun setCategoriesAdapter(){
        val adapter=ProductFilterCategoryAdapter(productFilterViewModel.categoryList)
        binding.customizationTypeRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.customizationTypeRecyclerView.adapter=adapter
    }

    private fun setSubCategoriesAdapter(){
        /*val newList=productFilterViewModel.subCategoryList.filter { if(productFilterViewModel.v) }*/
        val adapter=ProductFilterSubCategoryAdapter(productFilterViewModel.subCategoryList)
        binding.customizationSubTypeRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.customizationSubTypeRecyclerView.adapter=adapter
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            productFilterViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {

                            is CategorieResponse ->{
                                productFilterViewModel.categoryList.addAll(event.response.data)
                                setCategoriesAdapter()
                               productFilterViewModel.getSubCategories()
                            }
                            is SubCategorieResponse ->{
                                productFilterViewModel.categoryList.addAll(event.response.data)
                                setSubCategoriesAdapter()
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(productFilterViewModel.methodRepo)
                        }
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }

    }
}