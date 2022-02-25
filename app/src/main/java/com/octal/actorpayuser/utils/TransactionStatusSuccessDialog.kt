package com.octal.actorpayuser.utils

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.AddTransactionStatusDialogBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.wallet.AddMoneyData

class TransactionStatusSuccessDialog(
    private val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val message:String,
    val addMoneyData: AddMoneyData,
    val onClick:(Clicks)->Unit
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
//        dialog.setCancelable(false)
        isCancelable = true

        binding.back.setOnClickListener {
            onClick(Clicks.BACK)
            dismiss()
        }
        binding.history.setOnClickListener {
            onClick(Clicks.DONE)
            dismiss()
        }
        binding.done.setOnClickListener {
            onClick(Clicks.Root)
            dismiss()
        }

            binding.paymentIcon.setAnimation("success_tick_lottie.json")
            binding.paymentIcon.playAnimation()
            binding.paymentStatusText.setText(message)
            binding.paymentTxn.text="Transaction Id: ${addMoneyData.transactionId}"


        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        onClick(Clicks.BACK)
        super.onDismiss(dialog)

    }
}