package com.octal.actorpay.base


import android.app.Activity
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.octal.actorpay.R
import com.octal.actorpay.ui.dashboard.bottomnavfragments.HomeBottomFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

abstract class BaseActivity : AppCompatActivity() {
    private var addToBackStack = false
    private var manager: FragmentManager? = null
    private var transaction: FragmentTransaction? = null
    private var fragment: Fragment? = null
    private var doubleBackToExitPressedOnce = false
    private lateinit var snackBar: Snackbar
    private lateinit var actiVityView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this,R.color.primary)
    }
    open fun setView(view: View){
        this.actiVityView=view
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


    open fun startFragment(fragment: Fragment?, backStackTag: String?, addToBackStack: Boolean) {
          if(manager==null){
              manager = supportFragmentManager
          }
        transaction = manager!!.beginTransaction()
        this.addToBackStack = addToBackStack
        transaction!!.addToBackStack(backStackTag)
        transaction!!.replace(R.id.container, fragment!!)
        if (!isFinishing && !isDestroyed) {
            transaction!!.commit()
        }
    }

    public fun startFragment(fragment: Fragment?, addToBackStack: Boolean, backStackTag: String?) {
        this.addToBackStack = addToBackStack
        val fragmentPopped = manager!!.popBackStackImmediate(backStackTag, 0)
        if (!fragmentPopped) {
            transaction = manager!!.beginTransaction()
            if (addToBackStack) {
                transaction!!.addToBackStack(backStackTag)
            } else {
                transaction!!.addToBackStack(null)
            }
            transaction!!.replace(R.id.container, fragment!!)
            transaction!!.commit()
        }
    }



    open fun showCustomAlert(msg: String?, v: View?) {
        snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    override fun onBackPressed() {
        fragment = getCurrentFragment()
        if (addToBackStack) {
            if (fragment is HomeBottomFragment) {
                if (doubleBackToExitPressedOnce) {
                    finish()
                    return
                }
                doubleBackToExitPressedOnce = true
                showCustomAlert("Press back again",actiVityView);
                //Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT)
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(2000)
                    doubleBackToExitPressedOnce = false
                }
            } else {
                if (manager!= null && manager!!.backStackEntryCount > 0) {
                    manager!!.popBackStackImmediate()
                } else {
                    super.onBackPressed()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    open fun getCurrentFragment(): Fragment? {
        fragment = manager!!.findFragmentById(R.id.container)
        return fragment
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



    fun setFragment(resourceView: Int, fragment: Fragment, addToBackStackFlag: Boolean) {
        //
        val fragmentManager: FragmentManager = supportFragmentManager
        val mFragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val currentFragment = fragmentManager.findFragmentById(resourceView)

        if (currentFragment == null) {
            if (fragmentManager.fragments.size != 0) {
                if (addToBackStackFlag) {
                    mFragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
                }

                mFragmentTransaction.replace(resourceView, fragment)
                mFragmentTransaction.commit()
            }
        } else {
            if (!(currentFragment.javaClass.simpleName.equals(fragment.javaClass.simpleName))) {
                if (fragmentManager.fragments.size != 0) {
                    if (addToBackStackFlag) {
                        mFragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
                    }
                    mFragmentTransaction.replace(resourceView, fragment)
                    mFragmentTransaction.commit()
                }
            }

        }
    }

    /**
     * override onBackPressed
     *
     */
  /*  @Override
    override fun onBackPressed() {
        val fm = supportFragmentManager
        val backStackCount = fm.backStackEntryCount

        if (backStackCount == 0) {
            finish()
        } else if (backStackCount > 1) {
            val backStackEntry = fm.getBackStackEntryAt(backStackCount - 1)
            val frag = fm.findFragmentByTag(backStackEntry.name)

            if (frag!!.childFragmentManager.backStackEntryCount > 1) {
                frag.childFragmentManager.popBackStack()
            } else {
                fm.popBackStack()
            }

        } else {
            val backStackEntry = fm.getBackStackEntryAt(fm.backStackEntryCount - 1)
            val frag = fm.findFragmentByTag(backStackEntry.name)
            if (frag!!.childFragmentManager.backStackEntryCount > 1) {
                frag.childFragmentManager.popBackStack()
            } else {
               super.onBackPressed()
            }
        }
    }*/





    protected fun showSnackBar(message: String?) {
        Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
    }

    private val view: View
        get() = findViewById(R.id.content)


}