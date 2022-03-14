package com.octal.actorpayuser.repositories.methods
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.octal.actorpayuser.R
import com.octal.actorpayuser.database.datastore.DataStoreBase
import com.octal.actorpayuser.repositories.AppConstance.AppConstance
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern


class MethodsRepo(private var context: Context, var dataStore: DataStoreBase) {
    private var  progressDialog:Dialog?=null

    fun showCustomToast(msg: String) {
        val myToast = Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        )
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.show()
    }

    fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        if(Pattern.matches("[0-9]+", phone)) {
            return phone.length > 6 && phone.length <= 13
        }
        return false
    }
    fun isValidName(name: String):Boolean{
        val pattern = Pattern.compile("^[a-zA-Z\\s]*$")
        val matcher: Matcher = pattern.matcher(name)
        return matcher.matches()
    }
    fun isValidPassword(password: String):Boolean{
        val pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&*^()-_]).{8,20}$")
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
    fun isValidPAN(pan:String):Boolean{
        val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher: Matcher = pattern.matcher(pan)
        return matcher.matches()
    }
    fun makeTextLink(textView: TextView, str: String, underlined: Boolean, color: Int?, action: (() -> Unit)? = null) {
        val spannableString = SpannableString(textView.text)
        val textColor = color ?: textView.currentTextColor
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                action?.invoke()
            }
            override fun updateDrawState(drawState: TextPaint) {
                super.updateDrawState(drawState)
                drawState.isUnderlineText = underlined
                drawState.color = textColor
            }
        }
        val index = spannableString.indexOf(str)
        spannableString.setSpan(clickableSpan, index, index + str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }
      fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ))
        }
        return false
    }

    fun getFormattedOrderDate(orderDate: String): String? {
        try {
        return  AppConstance.dateFormate4.format(AppConstance.dateFormate3.parse(orderDate)!!)
        }
        catch (e : Exception){
            return orderDate
        }
    }




    fun setBackGround(context: Context?, view: View, @DrawableRes drawable: Int) {
        view.setBackground(ContextCompat.getDrawable(context!!, drawable))
    }

    fun hideSoftKeypad(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    } fun getDeviceWidth(context: Activity): Int {
//        val displayMetrics = DisplayMetrics()
//        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//        return displayMetrics.widthPixels

        val displayMetrics=Resources.getSystem().displayMetrics

        return displayMetrics.widthPixels
    }
    fun getDeviceHeight(context: Activity): Int {
        val displayMetrics=Resources.getSystem().displayMetrics
        return displayMetrics.heightPixels
    }
    fun showPopUpWindow(root: View,text: String){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_layout, null)

        val textView=view.findViewById<TextView>(R.id.pop_up_text)
        textView.text=text

        val mpopup = PopupWindow(
            view, ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, true
        ) // Creation of popup
        mpopup.showAsDropDown(root, 0, -(root.getHeight()+50))

        Handler(Looper.myLooper()!!).postDelayed({
                              mpopup.dismiss()
        },4000)
    }

    fun checkPermission(activity: Activity,permission:String):Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) != PackageManager.PERMISSION_DENIED
    }
}