package com.octal.actorpayuser.base


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.ProgressDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.ui.auth.LoginActivity
import com.octal.actorpayuser.utils.CommonDialogsUtils

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var snackBar: Snackbar
    private var  progressDialog: Dialog?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this,R.color.primary)
    }
    fun showCustomToast(msg: String,length:Int=Toast.LENGTH_LONG) {
        val myToast = Toast.makeText(
            applicationContext,
            msg,
            length
        )
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.show()
    }


    fun logout(methodRepo: MethodsRepo){
        CommonDialogsUtils.showCommonDialog(this,methodRepo, "Logout ",
            "Are you sure?", true, true, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
//                    viewModel.shared.Logout()
                    lifecycleScope.launchWhenCreated {

                        startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
                        methodRepo.dataStore.logOut()
                        methodRepo.dataStore.setIsIntro(true)
                        finishAffinity()

                    }
                }
                override fun onCancel() {
                }
            })
    }
    fun forcelogout(methodRepo: MethodsRepo){
        CommonDialogsUtils.showCommonDialog(this,methodRepo, "Log Out ",
            "Session Expire", false, false, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
//                    viewModel.shared.Logout()
                    lifecycleScope.launchWhenCreated {
                        methodRepo.dataStore.logOut()
                        methodRepo.dataStore.setIsIntro(true)
                        startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
                override fun onCancel() {
                }
            })
    }



    open fun showCustomAlert(msg: String?, v: View?) {
        snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }



    open fun hideSoftKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showLoadingDialog() {
        if(progressDialog==null){
            progressDialog = Dialog(this)
            if (progressDialog!!.window != null) {
                val window = progressDialog!!.window
                window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                window.setGravity(Gravity.CENTER)
                progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            val binding=DataBindingUtil.inflate<ProgressDialogBinding>(layoutInflater,R.layout.progress_dialog,null,false)
            progressDialog!!.setContentView(binding.root)
//            Glide.with(this).load(R.drawable.bar_loader).error(R.drawable.logo)
//                .into(binding.imgLoading)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
        }
        else{
            progressDialog!!.show()
        }

    }
    fun hideLoadingDialog() {
        if(progressDialog!=null){
            progressDialog!!.dismiss()
        }
    }


}