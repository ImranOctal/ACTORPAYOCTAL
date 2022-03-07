package com.octal.actorpayuser.ui.dashboard.bottomnavfragments.wallet.walletuser

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RequestMoneyDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo

class RequestMoneyDialog(
    val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val onClick: (amount:String,reason:String) -> Unit
) : Dialog(mContext) {
    override fun show() {
        super.show()
        window?.setGravity(Gravity.BOTTOM)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.order_filter_dialog)
        val binding = DataBindingUtil.inflate<RequestMoneyDialogBinding>(
            mContext.layoutInflater,
            R.layout.request_money_dialog,
            null,
            false
        )
        setContentView(binding.root)
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))





        binding.apply.setOnClickListener {
            val amount: String = binding.requestAmount.text.toString().trim()
            val reason = binding.requestReason.text.toString().trim()
            var isValidate=true

            if (amount.equals("")) {
                binding.requestAmount.error = "Please Enter Amount"
                binding.requestAmount.requestFocus()
                isValidate=false
            } else if (amount.toDouble() < 1) {
                binding.requestAmount.error = "Amount should not less 1"
                binding.requestAmount.requestFocus()
                isValidate=false
            }

            if(reason == ""){
                binding.requestReason.error = "Please Enter Reason"
                isValidate=false
            }
            if(isValidate)
            {
                onClick(amount,reason)
                dismiss()
            }


        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.reset.setOnClickListener {
            dismiss()
        }


    }
}