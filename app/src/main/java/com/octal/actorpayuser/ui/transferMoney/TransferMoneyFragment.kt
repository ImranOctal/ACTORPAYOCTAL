package com.octal.actorpayuser.ui.transferMoney

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.FragmentTransferMoneyBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_CONTACT
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_EMAIL
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_KEY
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_MOBILE
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_NAME
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_QR
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.KEY_TYPE
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.LoginResponses
import com.octal.actorpayuser.repositories.retrofitrepository.models.auth.login.UserDetailsResponse
import com.octal.actorpayuser.ui.dummytransactionprocess.DummyTransactionProcessDialog
import com.octal.actorpayuser.ui.dummytransactionprocess.DummyTransactionStatusDialog
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class TransferMoneyFragment : BaseFragment() {
    private lateinit var binding: FragmentTransferMoneyBinding
    private val transferMoneyViewModel: TransferMoneyViewModel by inject()

    private var isWalletToWallet = true
    private var permissions = Manifest.permission.CAMERA
    lateinit var codeScanner: CodeScanner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_money, container, false)
        init()
        apiResponse()

        codeScanner = CodeScanner(requireContext(), binding.codeScannerView)
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                binding.scan.visibility = View.VISIBLE
                codeScanner.stopPreview()
                showCustomToast("Scan result: ${it.text}")
                val bundle =
                    bundleOf(KEY_KEY to KEY_QR, KEY_NAME to "John")
                Navigation.findNavController(requireView())
                    .navigate(R.id.payFragment, bundle)

            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            requireActivity().runOnUiThread {
                showCustomToast("Camera initialization error: ${it.message}")
            }
        }


        binding.scan.setOnClickListener {

            if (!transferMoneyViewModel.methodRepo.checkPermission(
                    requireActivity(),
                    permissions
                )
            ) {
                permReqLauncher.launch(permissions)
            } else {
                binding.scan.visibility = View.GONE
                codeScanner.startPreview()
            }

        }
        binding.emailNumberField.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validate(true, "","")
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false

        }
        binding.payNow.setOnClickListener {
            DummyTransactionProcessDialog(
                requireActivity(),
                transferMoneyViewModel.methodRepo
            ) { action ->
                when (action) {
                    Clicks.Success -> {
                        binding.beneficiaryName.setText("")
                        binding.beneficiaryAccountNo.setText("")
                        binding.beneficiaryIfsc.setText("")
                        binding.beneficiaryBranch.setText("")
                        binding.beneficiaryReason.setText("")
                        DummyTransactionStatusDialog(
                            requireActivity(),
                            transferMoneyViewModel.methodRepo,
                            true
                        ).show(childFragmentManager, "status")
                    }
                    Clicks.Cancel -> {
                        DummyTransactionStatusDialog(
                            requireActivity(),
                            transferMoneyViewModel.methodRepo,
                            false
                        ).show(childFragmentManager, "status")
                    }
                    else -> Unit
                }
            }.show(childFragmentManager, "process")
        }

        return binding.root
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->

            if (permission) {
                binding.scan.visibility = View.GONE
                codeScanner.startPreview()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), permissions
                    )
                ) {
                    Toast.makeText(
                        requireContext(), "Permission Denied, Go to setting to give access",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }

            }
        }

    fun init() {
        binding.walletToWalletBtn.setOnClickListener {
            if (!isWalletToWallet) {
                isWalletToWallet = true
                binding.walletToWalletBtn.setBackgroundResource(R.drawable.round_wallet_bg)
                binding.walletToBankBtn.setBackgroundResource(R.drawable.round_wallet_blue_bg)
                binding.layoutScanQr.visibility = View.VISIBLE
                binding.layoutBank.visibility = View.GONE
            }
        }
        binding.walletToBankBtn.setOnClickListener {
            if (isWalletToWallet) {
                isWalletToWallet = false
                binding.walletToWalletBtn.setBackgroundResource(R.drawable.round_wallet_blue_bg)
                binding.walletToBankBtn.setBackgroundResource(R.drawable.round_wallet_bg)
                binding.layoutScanQr.visibility = View.GONE
                binding.layoutBank.visibility = View.VISIBLE
            }
        }
    }

    fun validate(checkAPI: Boolean, name: String,type:String,destination:Int=0) {
        transferMoneyViewModel.methodRepo.hideSoftKeypad(requireActivity())
        val contact = binding.emailNumberField.text.toString().trim()
        if (transferMoneyViewModel.methodRepo.isValidEmail(contact)) {
            if (checkAPI)
                transferMoneyViewModel.userExists(contact)
            else {
                val bundle =
                    bundleOf(KEY_KEY to KEY_EMAIL, KEY_CONTACT to contact, KEY_NAME to name,KEY_TYPE to type)
                Navigation.findNavController(requireView())
                    .navigate(destination, bundle)
            }
        } else if (transferMoneyViewModel.methodRepo.isValidPhoneNumber(contact)) {
            if (checkAPI)
                transferMoneyViewModel.userExists(contact)
            else {
                val bundle =
                    bundleOf(KEY_KEY to KEY_MOBILE, KEY_CONTACT to contact, KEY_NAME to name,KEY_TYPE to type)
                Navigation.findNavController(requireView())
                    .navigate(destination, bundle)
            }
        } else {
            binding.emailNumberField.error = "Please enter valid input"
        }
    }


    fun apiResponse() {

        lifecycleScope.launchWhenStarted {
            transferMoneyViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoading()
                    }
                    is ResponseSealed.Success -> {
                        hideLoading()
                        when (event.response) {
                            is UserDetailsResponse -> {
                                if(event.response.data.customerDetails!=null)
                                validate(
                                    false,
                                    event.response.data.customerDetails.firstName + " " + event.response.data.customerDetails.lastName,"customer",R.id.payFragment
                                )
                                else{
                                    validate(
                                        false,
                                        event.response.data.merchantDetails!!.businessName,"merchant",R.id.payFragment
                                    )
                                }
                            }
                        }
                        transferMoneyViewModel.responseLive.value = ResponseSealed.Empty
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        transferMoneyViewModel.responseLive.value = ResponseSealed.Empty
                        hideLoading()
                        if (event.message!!.code == 403) {
                            forcelogout(transferMoneyViewModel.methodRepo)
                        }
                        else if(event.message.message.contains("User is not found")){
                            validate(false, "","",R.id.referFragment)
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


}