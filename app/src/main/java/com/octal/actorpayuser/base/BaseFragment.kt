package com.octal.actorpayuser.base

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.octal.actorpayuser.MainActivity
import com.octal.actorpayuser.R
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.utils.OnFilterClick
import com.octal.actorpayuser.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment(){

    private lateinit var snackBar: Snackbar
    private var manager: FragmentManager? = null
    val viewModel: ActorPayViewModel by  inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = requireActivity().supportFragmentManager
    }

    open fun showCustomAlert(msg: String?, v: View?) {
        snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    fun onFilterClick(filterClick: OnFilterClick){
        (requireActivity() as MainActivity).onFilterClick(filterClick)
    }

    fun showCustomToast(msg: String) {
        val myToast = Toast.makeText(
            activity,
            msg,
            Toast.LENGTH_SHORT
        )
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.show()
    }

    fun logout(methodRepo: MethodsRepo){
        (requireActivity() as BaseActivity).logout(methodRepo)
    }

    fun forcelogout(methodRepo: MethodsRepo){
        (requireActivity() as BaseActivity).forcelogout(methodRepo)
    }
    fun showLoading(){
        (requireActivity() as BaseActivity).showLoadingDialog()
    }
    fun hideLoading(){
        (requireActivity() as BaseActivity).hideLoadingDialog()
    }


}