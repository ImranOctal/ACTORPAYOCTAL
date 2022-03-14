package com.octal.actorpayuser.ui.request_money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentRequestMoneyBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.GetAllRequestMoneyParams
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.GetAllRequestMoneyResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.RequestMoneyListData
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.RequestProcessResponse
import com.octal.actorpayuser.ui.myOrderList.OrderFilterDialog
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.utils.OnFilterClick
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class RequestMoneyFragment : BaseFragment(), OnFilterClick {
    private lateinit var binding: FragmentRequestMoneyBinding
    private val requestMoneyViewModel: RequestMoneyViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRequestMoneyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requestMoneyViewModel.requestMoneyListData.pageNumber = 0
        requestMoneyViewModel.requestMoneyListData.items.clear()
        requestMoneyViewModel.getAllRequest()

        apiResponse()
        setAdapter()
        onFilterClick(this)

        binding.emailNumberField.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validate(true, "")
                return@setOnEditorActionListener true;
            }
            return@setOnEditorActionListener false;
        }

        binding.messageRefresh.setDistanceToTriggerSync(50)
        binding.messageRefresh.setOnRefreshListener {
            requestMoneyViewModel.requestMoneyListData.pageNumber = 0
            requestMoneyViewModel.requestMoneyListData.items.clear()
            requestMoneyViewModel.getAllRequest()
            binding.messageRefresh.isRefreshing=false
        }


        return root
    }

    fun setAdapter() {
        lifecycleScope.launchWhenCreated {

            requestMoneyViewModel.methodRepo.dataStore.getUserId().collect { userId ->

                val adapter = RequestMoneyAdapter(
                    userId,
                    requestMoneyViewModel.methodRepo,
                    requestMoneyViewModel.requestMoneyListData.items
                ) {
                    click,position->

                        when(click){
                            Clicks.PAY ->{
                                processRequest(true,requestMoneyViewModel.requestMoneyListData.items[position].requestId)
                            }
                            Clicks.DECLINE->{
                                processRequest(false,requestMoneyViewModel.requestMoneyListData.items[position].requestId)
                            }
                            Clicks.Root->{
                                val bundle= bundleOf("item" to requestMoneyViewModel.requestMoneyListData.items[position])
                                Navigation.findNavController(requireView()).navigate(R.id.requestMoneyDetailsFragment,bundle)
                            }
                            else -> Unit
                        }
                }
                binding.rvRequestMoney.adapter = adapter
                val linearLayoutManager = LinearLayoutManager(requireContext())
                linearLayoutManager.reverseLayout = true
                binding.rvRequestMoney.layoutManager = linearLayoutManager

            }

        }
    }
    fun processRequest(isAccept:Boolean,requestId:String){
        var title=""
        if(isAccept)
            title="Pay User"
        else
            title="Decline Request"
        CommonDialogsUtils.showCommonDialog(requireActivity(),requestMoneyViewModel.methodRepo,title,"Are you sure?",true,true,
            true,false,object : CommonDialogsUtils.DialogClick{
                override fun onClick() {
                    requestMoneyViewModel.processRequest(isAccept,requestId)
                }
                override fun onCancel() {

                }
            })
    }

    fun validate(checkAPI: Boolean, name: String,destintation:Int=0) {
        requestMoneyViewModel.methodRepo.hideSoftKeypad(requireActivity())
        val contact = binding.emailNumberField.text.toString().trim()
        if (requestMoneyViewModel.methodRepo.isValidEmail(contact)) {
            if (checkAPI)
                requestMoneyViewModel.userExists(contact)
            else {
                val bundle =
                    bundleOf(
                        AppConstance.KEY_KEY to AppConstance.KEY_EMAIL,
                        AppConstance.KEY_CONTACT to contact,
                        AppConstance.KEY_NAME to name
                    )
                Navigation.findNavController(requireView())
                    .navigate(destintation, bundle)
            }
        } else if (requestMoneyViewModel.methodRepo.isValidPhoneNumber(contact)) {
            if (checkAPI)
                requestMoneyViewModel.userExists(contact)
            else {
                val bundle =
                    bundleOf(
                        AppConstance.KEY_KEY to AppConstance.KEY_MOBILE,
                        AppConstance.KEY_CONTACT to contact,
                        AppConstance.KEY_NAME to name
                    )
                Navigation.findNavController(requireView())
                    .navigate(destintation, bundle)
            }
        } else {
            binding.emailNumberField.error = "Please enter valid input"
        }
    }

    fun updateUI(requestMoneyListData: RequestMoneyListData) {
        requestMoneyViewModel.requestMoneyListData.pageNumber =
            requestMoneyListData.pageNumber
        requestMoneyViewModel.requestMoneyListData.totalPages =
            requestMoneyListData.totalPages
        requestMoneyViewModel.requestMoneyListData.items.addAll(requestMoneyListData.items)

        binding.rvRequestMoney.adapter?.notifyDataSetChanged()

        if (requestMoneyListData.pageNumber == 0)
            binding.rvRequestMoney.scrollToPosition(0)

        if (requestMoneyViewModel.requestMoneyListData.items.size > 0) {
            binding.imageEmpty.visibility = View.GONE
            binding.textEmpty.visibility = View.GONE
        } else {
            binding.imageEmpty.visibility = View.VISIBLE
            binding.textEmpty.visibility = View.VISIBLE
        }
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            requestMoneyViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is LoginResponses -> {
                                validate(
                                    false,
                                    event.response.data.firstName + " " + event.response.data.lastName,R.id.receiveFragment
                                )
                            }
                            is GetAllRequestMoneyResponse -> {
                                updateUI(event.response.data)
                            }
                            is RequestProcessResponse->{
                                requestMoneyViewModel.requestMoneyListData.pageNumber = 0
                                requestMoneyViewModel.requestMoneyListData.items.clear()
                                requestMoneyViewModel.getAllRequest()
                            }
                        }
                        requestMoneyViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        requestMoneyViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(requestMoneyViewModel.methodRepo)
                        }
                        else if(event.message.message.contains("User is not found")){
                            validate(false, "",R.id.referFragment)
                        }
                        else
                            showCustomToast(event.message.message)
                    }
                    is ResponseSealed.Empty -> {
                        hideLoading()

                    }
                }
            }
        }
    }

    override fun onClick() {
        RequestFilterDialog(
            requestMoneyViewModel.requestMoneyParams,
            requireActivity(),
            requestMoneyViewModel.methodRepo
        ) {
            requestMoneyViewModel.requestMoneyParams = it
            requestMoneyViewModel.requestMoneyListData.pageNumber = 0
            requestMoneyViewModel.requestMoneyListData.totalPages = 0
            requestMoneyViewModel.requestMoneyListData.items.clear()
//            requestMoneyViewModel.getAllRequest()

        }.show()
    }

}