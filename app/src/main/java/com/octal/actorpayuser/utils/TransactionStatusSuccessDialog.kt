package com.octal.actorpayuser.utils

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.AddTransactionStatusDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo

class TransactionStatusSuccessDialog(
    private val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val amount:Double,
    val message:String,
    val onClick:()->Unit
): DialogFragment() {

    lateinit var binding: AddTransactionStatusDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(mContext, R.style.MainDialog)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.add_transaction_status_dialog,
            null,
            false
        )
        dialog.setContentView(binding.root)
        isCancelable = true

        binding.back.setOnClickListener {
            dismiss()
        }
        binding.history.setOnClickListener {
            onClick()
        }

            binding.paymentIcon.setAnimation("success_tick_lottie.json")
            binding.paymentIcon.playAnimation()
            binding.paymentStatusText.setText(message)


        return dialog

    }
}