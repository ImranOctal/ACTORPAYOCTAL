package com.octal.actorpay.ui.misc

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.R
import com.octal.actorpay.databinding.ChangePasswordDialogBinding
import com.octal.actorpay.databinding.ForgetPasswordDialogBinding
import com.octal.actorpay.repositories.methods.MethodsRepo

class ChangePasswordDialog {


    fun show(activity: Activity, methodsRepo: MethodsRepo, onClick:(oldPassword:String,newPassword:String)->Unit){
        val alertDialog = Dialog(activity)

        val binding = DataBindingUtil.inflate<ChangePasswordDialogBinding>(
            activity.layoutInflater,
            R.layout.change_password_dialog,
            null,
            false
        )

        binding.tvOk.setOnClickListener {
            val oldPassword=binding.editChangePasswordOld.text.toString().trim()
            val newPassword=binding.editChangePasswordNew.text.toString().trim()
            val confirmPassword=binding.editChangePasswordConfirm.text.toString().trim()
            if(oldPassword.isEmpty()){
                binding.editChangePasswordOld.setError(activity.getString(R.string.oops_your_password_is_empty))
            }
            else if(newPassword.length<8)
            {
                binding.editChangePasswordNew.setError(activity.getString(R.string.oops_your_password_is_not_valid))
            }
            else if(!methodsRepo.isValidPassword(newPassword)){
                binding.editChangePasswordNew.setError(activity.getString(R.string.oops_your_password_is_not_valid2))
            }
            else if(!confirmPassword.equals(newPassword))
            {
                binding.editChangePasswordConfirm.setError((activity.getString(R.string.oops_your_password_is_not_matched)))
            }
            else{
                alertDialog.dismiss()
                onClick(oldPassword,newPassword)
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