package com.octal.actorpay.ui.auth.verifyotp

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.R
import com.octal.actorpay.databinding.ForgetPasswordDialogBinding
import com.octal.actorpay.databinding.OtpVerifyDialogBinding
import com.octal.actorpay.repositories.methods.MethodsRepo

class VerifyOtpDialog() {


    fun show(activity: Activity, methodsRepo: MethodsRepo, onClick:(email:String)->Unit){
        val alertDialog = Dialog(activity)

        val binding = DataBindingUtil.inflate<OtpVerifyDialogBinding>(
            activity.layoutInflater,
            R.layout.otp_verify_dialog,
            null,
            false
        )

        binding.tvOk.setOnClickListener {
            val otp=binding.edtOtp.text.toString().trim()
            if(otp.length!=6)
            {
                binding.edtOtp.error = activity.getString(R.string.invalid_otp)
            }
            else{
                alertDialog.dismiss()
                onClick(otp)
            }
        }
        binding.tvCancel.setOnClickListener {
            alertDialog.dismiss()
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
            setCanceledOnTouchOutside(true)

        }.also { it.show() }
    }
}