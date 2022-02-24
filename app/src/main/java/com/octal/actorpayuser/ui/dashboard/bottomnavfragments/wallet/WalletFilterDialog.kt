package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.wallet

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.WalletFilterDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.WallletMoneyParams

class WalletFilterDialog(
    private val params: WallletMoneyParams,
    val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val onClick: (WallletMoneyParams) -> Unit
) : Dialog(mContext) {
    override fun show() {
        super.show()
        window?.setGravity(Gravity.BOTTOM)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DataBindingUtil.inflate<WalletFilterDialogBinding>(
            mContext.layoutInflater,
            R.layout.wallet_filter_dialog,
            null,
            false
        )
        setContentView(binding.root)
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        ArrayAdapter.createFromResource(
            mContext,
            R.array.wallet_txn_type_status_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Apply the adapter to the spinner
            binding.spinnerStatus.adapter = adapter
        }
        binding.spinnerStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if(position==0){
                    (view as TextView).setTextColor(ContextCompat.getColor(mContext,R.color.gray))
                }
            }

        }




        binding.walletId.setText(params.walletTransactionId)
        binding.totalFrom.setText(params.transactionAmountFrom.toString())
        binding.totalTo.setText(params.transactionAmountTo.toString())
        binding.remark.setText(params.transactionRemark.toString())

        val array = mContext.resources.getStringArray(R.array.wallet_txn_type_status_array).toMutableList()

        if (array.contains(params.transactionType)) {
            val pos = array.indexOfFirst { it.equals(params.transactionType) }
            binding.spinnerStatus.setSelection(pos)
        }



        binding.apply.setOnClickListener {
            var wallet: String = ""
            var totalFrom: String = ""
            var totalTo: String = ""
            var remark: String = ""
            var status: String = ""


            wallet = binding.walletId.text.toString().trim()

            totalFrom = binding.totalFrom.text.toString().trim()

            totalTo = binding.totalTo.text.toString().trim()

            remark = binding.remark.text.toString().trim()

            val statusPosition = binding.spinnerStatus.selectedItemPosition
            if (statusPosition != 0) {
                status = array[statusPosition]
            }

            onClick(
                WallletMoneyParams(
                    wallet, totalTo, totalFrom, remark,status
                )
            )
            dismiss()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.reset.setOnClickListener {
            binding.walletId.setText("")
            binding.totalFrom.setText("")
            binding.totalTo.setText("")
            binding.remark.setText("")
            binding.spinnerStatus.setSelection(0)
        }


    }
}