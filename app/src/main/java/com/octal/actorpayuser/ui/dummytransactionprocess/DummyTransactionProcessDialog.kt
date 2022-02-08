package com.octal.actorpayuser.ui.dummytransactionprocess

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.TransactionProcessDialogBinding
import com.octal.actorpayuser.repositories.AppConstance.Clicks
import com.octal.actorpayuser.repositories.methods.MethodsRepo

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