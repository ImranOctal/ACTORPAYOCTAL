package com.octal.actorpay.base


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.octal.actorpay.R
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.ui.dashboard.bottomnavfragments.HomeBottomFragment
import com.octal.actorpay.utils.CommonDialogsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

abstract class BaseActivity : AppCompatActivity() {
    private var addToBackStack = false
    private var manager: FragmentManager? = null
    private var transaction: FragmentTransaction? = null
    private lateinit var snackBar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this,R.color.primary)
    }
    fun showCustomToast(msg: String) {
        val myToast = Toast.makeText(
            applicationContext,
            msg,
            Toast.LENGTH_SHORT
        )
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.show()
    }


    fun showMsg(connected: Boolean) {
        if (connected == false) {
            val myToast = Toast.makeText(
                applicationContext,
                getString(R.string.not_connected_internet),
                Toast.LENGTH_SHORT
            )
            myToast.setGravity(Gravity.CENTER, 0, 0)
            myToast.show()
            //showAlertBar(getString(R.string.not_connected_internet))
        }

    }

    fun logout(methodRepo: MethodsRepo){
        CommonDialogsUtils.showCommonDialog(this,methodRepo, "Logout ",
            "Are you sure?", true, true, true, false,
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

    protected fun showSnackBar(message: String?) {
        Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
    }

    private val view: View
        get() = findViewById(R.id.content)


}