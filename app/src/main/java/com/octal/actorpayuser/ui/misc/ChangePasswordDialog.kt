package com.octal.actorpayuser.ui.misc

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.ChangePasswordDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo

class ChangePasswordDialog {

    private var showPasswordOld = false
    private var showPasswordNew = false
    private var showPasswordConfirm = false

    fun show(
        activity: Activity,
        methodsRepo: MethodsRepo,
        onClick: (oldPassword: String, newPassword: String) -> Unit
    ) {
        val alertDialog = Dialog(activity)

        val binding = DataBindingUtil.inflate<ChangePasswordDialogBinding>(
            activity.layoutInflater,
            R.layout.change_password_dialog,
            null,
            false
        )

        binding.editChangePasswordOld.doOnTextChanged { text, start, before, count ->

            val password = text.toString()
            var temp = ""
            if (!methodsRepo.isSpecialCharacter(password))
                temp = activity.getString(R.string.error_password_special)
            if (!methodsRepo.isDigit(password))
                temp = activity.getString(R.string.error_password_digit)
            if (!methodsRepo.isSmallLetter(password))
                temp = activity.getString(R.string.error_password_small)
            if (!methodsRepo.isCapitalLetter(password))
                temp = activity.getString(R.string.error_password_capital)
            if (password.length < 8)
                temp = activity.getString(R.string.oops_your_password_is_not_valid)


            if (temp != "" && password.length != 0) {
                binding.errorOnPassword.visibility = View.VISIBLE
                binding.errorOnPassword.text = temp
            } else {
                binding.errorOnPassword.visibility = View.GONE
                binding.errorOnPassword.text = ""
            }
        }
        binding.editChangePasswordNew.doOnTextChanged { text, start, before, count ->

            val password = text.toString()
            var temp = ""
            if (!methodsRepo.isSpecialCharacter(password))
                temp = activity.getString(R.string.error_password_special)
            if (!methodsRepo.isDigit(password))
                temp = activity.getString(R.string.error_password_digit)
            if (!methodsRepo.isSmallLetter(password))
                temp = activity.getString(R.string.error_password_small)
            if (!methodsRepo.isCapitalLetter(password))
                temp = activity.getString(R.string.error_password_capital)
            if (password.length < 8)
                temp = activity.getString(R.string.oops_your_password_is_not_valid)


            if (temp != "" && password.length != 0) {
                binding.errorOnNewPassword.visibility = View.VISIBLE
                binding.errorOnNewPassword.text = temp
            } else {
                binding.errorOnNewPassword.visibility = View.GONE
                binding.errorOnNewPassword.text = ""
            }
        }

        binding.tvOk.setOnClickListener {
            val oldPassword = binding.editChangePasswordOld.text.toString().trim()
            val newPassword = binding.editChangePasswordNew.text.toString().trim()
            val confirmPassword = binding.editChangePasswordConfirm.text.toString().trim()
            var isValid = true

            if (confirmPassword != newPassword) {
                binding.editChangePasswordConfirm.error =
                    (activity.getString(R.string.oops_your_password_is_not_matched))
                binding.editChangePasswordConfirm.requestFocus()
                isValid=false
            }
            if (newPassword.length < 8) {
                binding.editChangePasswordNew.error =
                    activity.getString(R.string.oops_your_password_is_not_valid)
                binding.editChangePasswordNew.requestFocus()
                isValid = false
            } else if (!methodsRepo.isValidPassword(newPassword)) {
                binding.editChangePasswordNew.error =
                    activity.getString(R.string.oops_your_password_is_not_valid2)
                binding.editChangePasswordNew.requestFocus()
                isValid = false
            }


            if (oldPassword.isEmpty()) {
                binding.editChangePasswordOld.error =
                    activity.getString(R.string.oops_your_password_is_empty)
                binding.editChangePasswordOld.requestFocus()
                isValid = false
            } else if (!methodsRepo.isValidPassword(oldPassword)) {
                binding.editChangePasswordOld.error =
                    activity.getString(R.string.oops_your_password_is_not_valid2)
                binding.editChangePasswordOld.requestFocus()
                isValid = false
            }

           if(isValid) {
                alertDialog.dismiss()
                onClick(oldPassword, newPassword)
            }

            /*   else if(confirmPassword.length<8)
               {
                   binding.editChangePasswordConfirm.error = activity.getString(R.string.oops_your_password_is_not_valid)
                   binding.editChangePasswordConfirm.requestFocus()
               }
               else if(!methodsRepo.isValidPassword(confirmPassword)){
                   binding.editChangePasswordConfirm.error = activity.getString(R.string.oops_your_password_is_not_valid2)
                   binding.editChangePasswordConfirm.requestFocus()
               }*/


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
                binding.editChangePasswordConfirm.transformationMethod =
                    PasswordTransformationMethod()
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
                (methodsRepo.getDeviceWidth(activity) / 100) * 90,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(true)

        }.also { it.show() }
    }
}