package com.octal.actorpay.ui.dummytransactionprocess

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.octal.actorpay.R
import com.octal.actorpay.databinding.TransactionProcessDialogBinding
import com.octal.actorpay.repositories.AppConstance.Clicks
import com.octal.actorpay.repositories.methods.MethodsRepo
import java.io.File

class DummyTransactionProcessDialog(
    private val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val onDone: (action:Clicks) -> Unit,
):DialogFragment() {

    lateinit var binding: TransactionProcessDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog  {

        val dialog = Dialog(mContext, R.style.MainDialog)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.transaction_process_dialog,
            null,
            false
        )
        dialog.setContentView(binding.root)
        isCancelable = false

        binding.back.setOnClickListener {
            dismiss()
            onDone(Clicks.Cancel)
        }
        binding.success.setOnClickListener {
            dismiss()
            onDone(Clicks.Success)
        }
        binding.fail.setOnClickListener {
            dismiss()
            onDone(Clicks.Cancel)
        }

        return dialog
    }
}