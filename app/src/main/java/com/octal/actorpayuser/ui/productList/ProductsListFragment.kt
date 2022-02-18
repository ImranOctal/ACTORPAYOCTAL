package com.octal.actorpayuser.ui.productList

import android.content.Intent
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
import com.octal.actorpayuser.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieItem
import com.octal.actorpayuser.repositories.retrofitrepository.models.categories.CategorieResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.products.ProductData
import com.octal.actorpayuser.utils.OnFilterClick


class ProductsListFragment : BaseFragment(),OnFilterClick {
    private lateinit var binding: FragmentProductsListBinding
    private val productViewModel: ProductViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    private var isFromBuy=false
    private var futureAddCart=-1

    lateinit var adapter: ProductListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel.productData.pageNumber = 0
        productViewModel.productData.totalPages = 0
        productViewModel.productData.items.clear()
        productViewModel.name=""
        productViewModel.category=""
        productViewModel.getProducts()
        productViewModel.categoryList. clear()
        Handler(Looper.myLooper()!!).postDelayed({
            productViewModel.getCategories()
        },200)
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
                productViewModel.productData.pageNumber = 0
                productViewModel.productData.totalPages = 0
                productViewModel.productData.items.clear()
                productViewModel.getProducts()
                productViewModel.methodRepo.hideSoftKeypad(requireActivity())

            }
            false
        }


        return binding.root

    }


    private var resultLauncher = registerForActivityResult(StartActivityForResult()) {

        binding.recyclerviewProduct.adapter?.notifyDataSetChanged()

    }

    private fun cartResponse() {
        lifecycleScope.launchWhenCreated {

            cartViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        binding.recyclerviewProduct.adapter?.notifyDataSetChanged()
                        if(futureAddCart >= 0){
                            addToCart(futureAddCart)
                            futureAddCart=-1
                        }
                        if(isFromBuy) {
                            isFromBuy=false
                            goToCart()
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        futureAddCart=-1
                        if(isFromBuy)
                            isFromBuy=false
//                        if (event.message!!.code == 403) {
//                            forcelogout(productViewModel.methodRepo)
//                        }
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
                                  updateUI(event.response.data)
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

    private fun updateUI(productData: ProductData){
        productViewModel.productData.pageNumber =
            productData.pageNumber
        productViewModel.productData.totalPages =
            productData.totalPages
        productViewModel.productData.items.addAll(productData.items)
        binding.recyclerviewProduct.adapter?.notifyDataSetChanged()

        if(productViewModel.productData.items.size>0){
            binding.imageEmpty.visibility=View.GONE
            binding.textEmpty.visibility=View.GONE
        }
        else {
            binding.imageEmpty.visibility=View.VISIBLE
            binding.textEmpty.visibility=View.VISIBLE
        }
    }

    private fun setAdapter() {
        adapter = ProductListAdapter(
            requireContext(),
            productViewModel.productData.items,
            cartViewModel.cartItems
        ) { position, click ->
            val product=productViewModel.productData.items[position]
            when (click) {
                Clicks.Root -> {
                    val bundle= bundleOf("id" to product.productId)
                    Navigation.findNavController(requireView()).navigate(R.id.productDetailsFragment,bundle)
//                    val intent=Intent(requireActivity(),ProductDetailsActivity::class.java)
//                    intent.putExtra("id",product.productId)
//                    startActivity(intent)
                }
                Clicks.AddCart -> {
                    if (cartViewModel.cartItems.value.size > 0) {
                        val merchantId = cartViewModel.cartItems.value[0].merchantId
                        if (product.merchantId != merchantId) {
                            wantsToBuyDialog {
                                futureAddCart=position
                                cartViewModel.deleteAllCart()
                            }
                        } else
                            addToCart(position)
                    } else {
                        addToCart(position)
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
                                    futureAddCart=position
                                    cartViewModel.deleteAllCart()
                                }
                            } else {
                                isFromBuy=true
                                addToCart(position)
                            }
                         }
                    } else {
                        isFromBuy=true
                        addToCart(position)
                    }
                }
                else -> Unit

            }

        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewProduct.layoutManager = layoutManager
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (productViewModel.productData.pageNumber < productViewModel.productData.totalPages - 1) {
                        productViewModel.productData.pageNumber += 1
                        productViewModel.getProducts()
                    }
                }
            }
        binding.recyclerviewProduct.addOnScrollListener(endlessRecyclerViewScrollListener)
        binding.recyclerviewProduct.adapter = adapter


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
                productViewModel.productData.pageNumber = 0
                productViewModel.productData.totalPages = 0
                productViewModel.productData.items.clear()
                binding.recyclerviewCategories.adapter?.notifyDataSetChanged()

                productViewModel.getProducts()
            }
            }


        binding.recyclerviewCategories.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerviewCategories.adapter=categoryAdapter

    }

    private fun addToCart(position: Int) {
        val product = productViewModel.productData.items[position]
        cartViewModel.addCart(product.productId,product.dealPrice)

    }

    private fun goToCart() {

//        val intent = Intent(requireActivity(), CartActivity::class.java)
//        resultLauncher.launch(intent)

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