package com.octal.actorpayuser.ui.promocodes

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentListPromoBinding
import com.octal.actorpayuser.repositories.retrofitrepository.models.promocodes.PromoData
import com.octal.actorpayuser.repositories.retrofitrepository.models.promocodes.PromoResponse
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class PromoListFragment : BaseFragment() {
    private lateinit var binding: FragmentListPromoBinding
    private val promoListViewModel: PromoListViewModel by inject()

    private lateinit var promoListAdapter: PromoListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promoListViewModel.getPromos()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_promo, container, false)

        setAdapter()
        apiResponse()



        return binding.root
    }

    fun setAdapter(){
        promoListAdapter=PromoListAdapter(requireContext(), promoListViewModel.promoData.items){
            text->
            val clipboard: ClipboardManager? =
                ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
            val clip = ClipData.newPlainText("Promo Code", text)
            clipboard!!.setPrimaryClip(clip)
            showCustomToast("Code has been copied")
        }
        binding.recyclerviewPromo.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerviewPromo.adapter=promoListAdapter
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            promoListViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is PromoResponse -> {
                               updateUI(event.response.data)
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(promoListViewModel.methodRepo)
                        }
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }


    }
    private fun updateUI(promoData: PromoData){
        promoListViewModel.promoData.pageNumber =
            promoData.pageNumber
        promoListViewModel.promoData.totalPages =
            promoData.totalPages
        promoListViewModel.promoData.items.addAll(promoData.items)
        promoListAdapter.notifyItemChanged(promoListViewModel.promoData.items.size - 1)

        if(promoListViewModel.promoData.items.size==0){
            binding.imageEmpty.visibility=View.VISIBLE
            binding.textEmpty.visibility=View.VISIBLE
        }
        else{
            binding.imageEmpty.visibility=View.GONE
            binding.textEmpty.visibility=View.GONE
        }
    }
}