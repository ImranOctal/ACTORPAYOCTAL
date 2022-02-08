package com.octal.actorpayuser.ui.dummytransactionprocess

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.TransactionStatusDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo

class DummyTransactionStatusDialog(
    private val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val isSuccess:Boolean
): DialogFragment() {

    lateinit var binding: TransactionStatusDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(mContext, R.style.MainDialog)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.transaction_status_dialog,
            null,
            false
        )
        dialog.setContentView(binding.root)
        isCancelable = true

        binding.back.setOnClickListener {
            dismiss()
        }

        if(isSuccess){
            binding.paymentIcon.setImageResource(R.drawable.success_icon)
            binding.paymentStatus.text="Payment Succeed!"
            binding.paymentStatusText.visibility= View.VISIBLE
        }
        else{
            binding.paymentIcon.setImageResource(R.drawable.ic_baseline_error_outline_24)
            binding.paymentStatus.text="Payment Failed!"
            binding.paymentStatusText.visibility= View.GONE
        }

        return dialog

    }
}