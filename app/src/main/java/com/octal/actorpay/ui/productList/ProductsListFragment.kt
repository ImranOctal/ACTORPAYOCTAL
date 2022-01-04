package com.octal.actorpay.ui.productList

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.FragmentProductsListBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductListResponse
import com.octal.actorpay.ui.cart.CartActivity
import com.octal.actorpay.ui.cart.CartViewModel
import com.octal.actorpay.ui.productList.productdetails.ProductDetailsActivity
import com.octal.actorpay.utils.CommonDialogsUtils
import com.techno.taskmanagement.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.octal.actorpay.repositories.retrofitrepository.models.categories.CategorieItem
import com.octal.actorpay.repositories.retrofitrepository.models.categories.CategorieResponse
import com.octal.actorpay.repositories.retrofitrepository.models.products.ProductData
import com.octal.actorpay.ui.productList.productsfilter.ProductFilterFragment
import com.octal.actorpay.ui.shippingaddress.details.ShippingAddressDetailsFragment
import com.octal.actorpay.utils.OnFilterClick


class ProductsListFragment : BaseFragment(),OnFilterClick {
    private lateinit var binding: FragmentProductsListBinding
    private val productViewModel: ProductViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    var isFromBuy=false

    lateinit var adapter: ProductListAdapter

    override fun WorkStation() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel.productData.pageNumber = 0
        productViewModel.productData.totalPages = 0
        productViewModel.productData.items.clear()
        productViewModel.name=""
        productViewModel.category=""
        productViewModel.getProducts()
        productViewModel.categoryList.clear()
        Handler(Looper.myLooper()!!).postDelayed({
            productViewModel.getCategories()
        },200)
    }

    companion object {
        private var instance: ProductsListFragment? = null

        @JvmStatic
        fun newInstance(): ProductsListFragment? {

            if (instance == null) {
                instance = ProductsListFragment()
            }
            return instance
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_products_list, container, false)
        showHideBottomNav(false)
        showHideCartIcon(false)
        showHideFilterIcon(true)
        onFilterClick(this)
        setCategoriesAdapter()
        setAdapter()
        cartResponse()
        apiResponse()

        binding.productSearch.setOnEditorActionListener(object :TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val name=binding.productSearch.text.toString().trim()
                    productViewModel.name=name
                    productViewModel.productData.pageNumber = 0
                    productViewModel.productData.totalPages = 0
                    productViewModel.productData.items.clear()
                    productViewModel.getProducts()
                    productViewModel.methodRepo.hideSoftKeypad(requireActivity())

                }
                return false;
            }

        })


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
                        cartViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ResponseSealed.Success -> {
                        binding.recyclerviewProduct.adapter?.notifyDataSetChanged()
                        if(isFromBuy) {
                            isFromBuy=false
                            goToCart()
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        if(isFromBuy)
                            isFromBuy=false
                        if (event.message!!.code == 403) {
                            forcelogout(productViewModel.methodRepo)
                        }
                        cartViewModel.methodRepo.hideLoadingDialog()
                    }
                    is ResponseSealed.Empty -> {
                        cartViewModel.methodRepo.hideLoadingDialog()

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
                            productViewModel.methodRepo.showLoadingDialog(requireContext())
                        }
                        is ResponseSealed.Success -> {
                            productViewModel.methodRepo.hideLoadingDialog()
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
                            if (event.message!!.code == 403) {
                                forcelogout(productViewModel.methodRepo)
                            }
                            productViewModel.methodRepo.hideLoadingDialog()
                        }
                        is ResponseSealed.Empty -> {
                            productViewModel.methodRepo.hideLoadingDialog()

                        }
                    }
                }
            }

    }

    fun updateUI(productData: ProductData){
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
                    ProductDetailsActivity.productItem =
                        product
                    startActivity(Intent(requireActivity(), ProductDetailsActivity::class.java))
                }
                Clicks.AddCart -> {
                    if (cartViewModel.cartItems.value.size > 0) {
                        val merchantId = cartViewModel.cartItems.value[0].merchantId
                        if (product.merchantId != merchantId) {
                            wantsToBuyDialog {
//                                addToCart(position)
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
                                wantsToBuyDialog {
//                                    buyNow(position)
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

    fun setCategoriesAdapter(){

        val categorieAdapter=CategorieAdapter(productViewModel.categoryList){
            if(productViewModel.categoryList[it].isSelected.not()){
                productViewModel.categoryList.forEach(){
                    it.isSelected=false
                }
                var cat=productViewModel.categoryList[it].name
                if(cat.equals("All"))
                    cat=""
                productViewModel.category=cat
                productViewModel.categoryList[it].isSelected=true
                productViewModel.productData.pageNumber = 0
                productViewModel.productData.totalPages = 0
                productViewModel.productData.items.clear()
                binding.recyclerviewCategories.adapter?.notifyDataSetChanged()

                productViewModel.getProducts()
            }
            }


        binding.recyclerviewCategories.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerviewCategories.adapter=categorieAdapter

    }

    private fun addToCart(position: Int) {
        val product = productViewModel.productData.items[position]
        cartViewModel.addCart(product.productId,product.dealPrice)

    }

    fun goToCart() {

        val intent = Intent(requireActivity(), CartActivity::class.java)
        resultLauncher.launch(intent)

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
        startFragment(
            ProductFilterFragment.newInstance(),
            true,
            ProductFilterFragment.toString())
    }
}