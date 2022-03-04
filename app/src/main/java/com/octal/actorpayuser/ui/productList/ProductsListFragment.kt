package com.octal.actorpayuser.ui.productList

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentProductsListBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpayuser.ui.cart.CartViewModel
import com.octal.actorpayuser.utils.CommonDialogsUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.paging.LoadState
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductData
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductItem
import com.octal.actorpayuser.utils.OnFilterClick
import com.squareup.moshi.Json
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONObject


class ProductsListFragment : BaseFragment(),OnFilterClick {
    private lateinit var binding: FragmentProductsListBinding
    private val productViewModel: ProductViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    private var isFromBuy=false
    private var futureAddCart:ProductItem?=null

    lateinit var productPagingAdapter:ProductPagingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel.pageNumber=0
        productViewModel.name=""
        productViewModel.category=""
        productViewModel.categoryList. clear()
        productViewModel.getCategories()
        productPagingAdapter = ProductPagingAdapter(
            requireContext(),
            cartViewModel.cartItems){
                click,product ->

            when (click) {
                Clicks.Root -> {
                    val bundle= bundleOf("id" to product.productId)
                    Navigation.findNavController(requireView()).navigate(R.id.productDetailsFragment,bundle)
                }
                Clicks.AddCart -> {
                    if (cartViewModel.cartItems.value.size > 0) {
                        val merchantId = cartViewModel.cartItems.value[0].merchantId
                        if (product.merchantId != merchantId) {
                            wantsToBuyDialog {
                                futureAddCart=product
                                cartViewModel.deleteAllCart()
                            }
                        } else
                            addToCart(product)
                    } else {
                        addToCart(product)
                    }
                }
                Clicks.BuyNow -> {
                    if (cartViewModel.cartItems.value.size > 0) {
                        val cartItem=  cartViewModel.cartItems.value.find {
                            it.productId==product.productId
                        }
                        if(cartItem!=null)
                            goToCart()
                        else{
                            val merchantId = cartViewModel.cartItems.value[0].merchantId
                            if (product.merchantId != merchantId) {
                                wantsToBuyDialog{
                                    isFromBuy=true
                                    futureAddCart=product
                                    cartViewModel.deleteAllCart()
                                }
                            } else {
                                isFromBuy=true
                                addToCart(product)
                            }
                        }
                    } else {
                        isFromBuy=true
                        addToCart(product)
                    }
                }
                else -> Unit

            }
        }
        getProducts()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_products_list, container, false)

        onFilterClick(this)
        setCategoriesAdapter()
        setAdapter()
        cartResponse()
        apiResponse()

        binding.productSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val name = binding.productSearch.text.toString().trim()
                productViewModel.name = name
                productViewModel.pageNumber=0

                getProducts()
                productViewModel.methodRepo.hideSoftKeypad(requireActivity())

            }
            false
        }


        return binding.root

    }


    private fun cartResponse() {
        lifecycleScope.launchWhenCreated {

            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        if(event.isLoading)
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        binding.recyclerviewProduct.adapter?.notifyDataSetChanged()
                        updateCartCount(cartViewModel.cartData!!.totalQuantity)
                        if(futureAddCart !=null){
                            addToCart(futureAddCart!!)
                            futureAddCart=null
                        }
                        if(isFromBuy) {
                            isFromBuy=false
                            goToCart()
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        futureAddCart=null
                        if(isFromBuy)
                            isFromBuy=false
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }

                }
            }
        }
    }

    fun apiResponse() {

            lifecycleScope.launchWhenStarted {

                productViewModel.responseLive.collect { event ->
                    when (event) {
                        is ResponseSealed.loading -> {
                            showLoading()
                        }
                        is ResponseSealed.Success -> {
                            hideLoading()
                             productViewModel.responseLive.value=ResponseSealed.Empty
                            when (event.response) {
                                is ProductListResponse -> {

                                }
                                is CategorieResponse->{
                                    productViewModel.categoryList.add(CategorieItem("","All","","",true))
                                    productViewModel.categoryList.addAll(event.response.data)
                                   setCategoriesAdapter()
                                }
                            }
                        }
                        is ResponseSealed.ErrorOnResponse -> {
                            hideLoading()
                            if (event.message!!.code == 403) {
                                forcelogout(productViewModel.methodRepo)
                            }
                        }
                        is ResponseSealed.Empty -> {
                            hideLoading()

                        }
                    }
                }
            }
    }

    private fun getProducts(){
        lifecycleScope.launchWhenCreated {
            productViewModel.methodRepo.dataStore.getAccessToken().collect { token ->
                productViewModel.getProductsWithPaging(token).collectLatest {
                      productPagingAdapter.submitData(it)
                }
            }
        }
    }


    private fun setAdapter() {



       binding.recyclerviewProduct.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productPagingAdapter
        }
        productPagingAdapter.addLoadStateListener {
            state->
            when(state.refresh)
            {
                 is LoadState.Loading->{
                    showLoading()
                }
                is LoadState.NotLoading->
                {
                    hideLoading()
                    if(productPagingAdapter.itemCount>0){
                        binding.imageEmpty.visibility=View.GONE
                        binding.textEmpty.visibility=View.GONE
                    }
                    else {
                        binding.imageEmpty.visibility=View.VISIBLE
                        binding.textEmpty.visibility=View.VISIBLE
                    }
                }
                is LoadState.Error->{
                    hideLoading()
                    val errorResponse = (state.refresh as LoadState.Error).error.message!!
                    val json=JSONObject(errorResponse)
                    if (json.has("code") && json.getInt("code") == 403) {
                        forcelogout(productViewModel.methodRepo)
                    }
                }
            }
        }
        productPagingAdapter.refresh()


    }


    private fun setCategoriesAdapter(){

        val categoryAdapter=CategoryAdapter(productViewModel.categoryList){
            position->
            if(productViewModel.categoryList[position].isSelected.not()){
                productViewModel.categoryList.forEach{
                    it.isSelected=false
                }
                var cat=productViewModel.categoryList[position].name
                if(cat == "All")
                    cat=""
                productViewModel.category=cat
                productViewModel.categoryList[position].isSelected=true
                productViewModel.pageNumber=0
                binding.recyclerviewCategories.adapter?.notifyDataSetChanged()

                getProducts()
            }
            }


        binding.recyclerviewCategories.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerviewCategories.adapter=categoryAdapter

    }

    private fun addToCart(product: ProductItem) {
        cartViewModel.addCart(product.productId,product.dealPrice)
    }

    private fun goToCart() {
        Navigation.findNavController(requireView()).navigate(R.id.cartFragment)
    }

    private fun wantsToBuyDialog(onClick: () -> Unit) {
        CommonDialogsUtils.showCommonDialog(requireActivity(),
            productViewModel.methodRepo,
            "Replace Cart Item",
            "Your cart contains products from different Merchant, Do you want to discard the selection and add this product?",
            autoCancelable = true,
            isCancelAvailable = true,
            isOKAvailable = true,
            showClickable = false,
            callback = object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    onClick()
                }
                override fun onCancel() {

                }
            })
    }

    override fun onClick() {
        /*startFragment(
            ProductFilterFragment.newInstance(),
            true,
            ProductFilterFragment.toString())*/
    }
}