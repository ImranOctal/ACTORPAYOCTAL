package com.octal.actorpay.ui.productList

import android.content.Intent
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


class ProductsListFragment : BaseFragment() {
    private lateinit var binding: FragmentProductsListBinding
    private val productViewModel: ProductViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    var isFromBuy=false

    lateinit var adapter: ProductListAdapter

    override fun WorkStation() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel.productData.pageNumber = 0
        productViewModel.productData.items.clear()
        productViewModel.getProducts()
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
        setAdapter()
        cartResponse()
        apiResponse()


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
//                                productViewModel.productData.pageNumber=event.response.data.pageNumber
                                    productViewModel.productData.pageNumber =
                                        event.response.data.pageNumber
                                    productViewModel.productData.totalPages =
                                        event.response.data.totalPages
                                    productViewModel.productData.items.addAll(event.response.data.items)
                                    adapter.notifyItemChanged(productViewModel.productData.items.size - 1)
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

    private fun addToCart(position: Int) {
        val product = productViewModel.productData.items[position]
        cartViewModel.addCart(product.productId)

    }

    fun goToCart() {

        val intent = Intent(requireActivity(), CartActivity::class.java)
        resultLauncher.launch(intent)

    }

    private fun wantsToBuyDialog(onClick: () -> Unit) {
        CommonDialogsUtils.showCommonDialog(requireActivity(),
            productViewModel.methodRepo,
            "Replace Cart Item",
            "Your Cart Contains Products from Different Merchant, Do you want to discard the selection and add this product?",
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
}