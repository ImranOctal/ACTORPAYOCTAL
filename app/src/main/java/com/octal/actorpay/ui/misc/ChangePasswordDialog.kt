package com.octal.actorpay.ui.misc

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.method.PasswordTransformationMethod
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.R
import com.octal.actorpay.databinding.ChangePasswordDialogBinding
import com.octal.actorpay.databinding.ForgetPasswordDialogBinding
import com.octal.actorpay.repositories.methods.MethodsRepo

class ChangePasswordDialog {

    private var showPasswordOld = false
    private var showPasswordNew = false
    private var showPasswordConfirm = false

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
            else if(!methodsRepo.isValidPassword(oldPassword)){
                binding.editChangePasswordOld.setError(activity.getString(R.string.oops_your_password_is_not_valid2))
            }
            else if(newPassword.length<8)
            {
                binding.editChangePasswordNew.setError(activity.getString(R.string.oops_your_password_is_not_valid))
            }
            else if(!methodsRepo.isValidPassword(newPassword)){
                binding.editChangePasswordNew.setError(activity.getString(R.string.oops_your_password_is_not_valid2))
            }
            else if(confirmPassword.length<8)
            {
                binding.editChangePasswordConfirm.setError(activity.getString(R.string.oops_your_password_is_not_valid))
            }
            else if(!methodsRepo.isValidPassword(confirmPassword)){
                binding.editChangePasswordConfirm.setError(activity.getString(R.string.oops_your_password_is_not_valid2))
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
        binding.oldPasswordLayout.setOnClickListener {
            if (showPasswordOld) {
                binding.editChangePasswordOld.transformationMethod = PasswordTransformationMethod()
                showPasswordOld = false
                binding.passwordShowHideOld.setImageResource(R.drawable.show)
                binding.editChangePasswordOld.setSelection(binding.editChangePasswordOld.text.toString().length)
            } else {
                binding.editChangePasswordOld.transformationMethod = null
                showPasswordOld = true
                binding.passwordShowHideOld.setImageResource(R.drawable.hide)
                binding.editChangePasswordOld.setSelection(binding.editChangePasswordOld.text.toString().length)
            }
        }
        binding.newPasswordLayout.setOnClickListener {
            if (showPasswordNew) {
                binding.editChangePasswordNew.transformationMethod = PasswordTransformationMethod()
                showPasswordNew = false
                binding.passwordShowHideNew.setImageResource(R.drawable.show)
                binding.editChangePasswordNew.setSelection(binding.editChangePasswordNew.text.toString().length)
            } else {
                binding.editChangePasswordNew.transformationMethod = null
                showPasswordNew = true
                binding.passwordShowHideNew.setImageResource(R.drawable.hide)
                binding.editChangePasswordNew.setSelection(binding.editChangePasswordNew.text.toString().length)
            }
        }
        binding.confirmPasswordLayout.setOnClickListener {
            if (showPasswordConfirm) {
                binding.editChangePasswordConfirm.transformationMethod = PasswordTransformationMethod()
                showPasswordConfirm = false
                binding.passwordShowHideConfirm.setImageResource(R.drawable.show)
                binding.editChangePasswordConfirm.setSelection(binding.editChangePasswordConfirm.text.toString().length)
            } else {
                binding.editChangePasswordConfirm.transformationMethod = null
                showPasswordConfirm = true
                binding.passwordShowHideConfirm.setImageResource(R.drawable.hide)
                binding.editChangePasswordConfirm.setSelection(binding.editChangePasswordConfirm.text.toString().length)
            }
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