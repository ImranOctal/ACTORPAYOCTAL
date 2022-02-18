package com.octal.actorpayuser.ui.dispute

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.CommonDialogBinding
import com.octal.actorpayuser.databinding.RaiseDisputeSuccessDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.dispute.DisputeData

class RaiseDisputeSuccessDialog {

    fun showDialog(activity: Activity,
                   methodsRepo: MethodsRepo,
                   disputeData: DisputeData,
                   onClick:()->Unit
    )
    {
        val alertDialog = Dialog(activity)

        val binding = DataBindingUtil.inflate<RaiseDisputeSuccessDialogBinding>(
            activity.layoutInflater,
            R.layout.raise_dispute_success_dialog,
            null,
            false
        )

        binding.disputedata=disputeData

        binding.tvOk.setOnClickListener { alertDialog.dismiss()
            onClick()
        }


        alertDialog.apply {
            window?.attributes?.windowAnimations = R.style.DialogAnimationTheme
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window!!.setLayout(
                (methodsRepo.getDeviceWidth(context) / 100) * 90,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }.also { it.show() }

    }
}