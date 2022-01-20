package com.octal.actorpay.ui.productList.productsfilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.FragmentProductFilterBinding
import com.octal.actorpay.repositories.retrofitrepository.models.categories.CategorieResponse
import com.octal.actorpay.repositories.retrofitrepository.models.categories.SubCategorieResponse
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
                        productFilterViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ResponseSealed.Success -> {
                        productFilterViewModel.methodRepo.hideLoadingDialog()
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
                        if (event.message!!.code == 403) {
                            forcelogout(productFilterViewModel.methodRepo)
                        }
                        productFilterViewModel.methodRepo.hideLoadingDialog()
                    }
                    is ResponseSealed.Empty -> {
                        productFilterViewModel.methodRepo.hideLoadingDialog()

                    }
                }
            }
        }

    }
}