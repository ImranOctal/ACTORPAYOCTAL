package com.octal.actorpay.base

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.octal.actorpay.R
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment(){

    private lateinit var snackBar: Snackbar
    private var manager: FragmentManager? = null
    abstract fun WorkStation();
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
    open fun onBackPressed(){
        (requireActivity() as BaseActivity).onBackPressed()
    }
    open fun startFragment(fragment: Fragment?, backStackTag: String?, addToBackStack: Boolean) {
        (requireActivity() as BaseActivity).startFragment(fragment,backStackTag,true)
    }

    open fun startFragment(fragment: Fragment?, addToBackStack: Boolean, backStackTag: String?) {
        (requireActivity() as BaseActivity).startFragment(fragment,true,backStackTag)
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


}