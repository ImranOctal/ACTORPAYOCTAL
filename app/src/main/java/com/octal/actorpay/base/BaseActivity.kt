package com.octal.actorpay.base


import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.octal.actorpay.R
import java.util.regex.Pattern

abstract class BaseActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = this.resources.getColor(R.color.primary)
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

    /*Validation are Here*/
    fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length > 6 && phone.length <= 15
        } else false
    }

    fun setToolbarTitle(title: String, shouldShowBack: Boolean, shouldShowTitle: Boolean) {
        //val toolbar: Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)
        if (shouldShowTitle) {
            supportActionBar?.title = title
          //  toolbar.setTitleTextColor(resources.getColor(R.color.white))
            /*  if (title.equals(getString(R.string.edit_profile))) {
                  toolbar.setTitleTextColor(resources.getColor(R.color.black_filter_view))
                  showAlertBar("sdv;kmSD;KVD;KV;lsB")
              } else {*/
            // toolbar.setTitleTextColor(resources.getColor(R.color.white))
            /*  }*/
        } else {
            supportActionBar?.title = ""
        }
        if (shouldShowBack) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            //supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)
        }
    }


    fun logout(auth: String, contentTypeJson: String) {
        callToLogout(auth, contentTypeJson)
//        AppLocal.getInstance().clearConfig()
//        LoginActivity.startActivity(this@BaseActivity, null)
//        finishAffinity()
    }


    fun callToLogout(auth: String, contentTypeJson: String) {
        /*var restApiFactory: RestApiFactory? = RestApiFactory
        var responseLiveData = MutableLiveData<ApiResponse<Any>>()
        var apiResponse: ApiResponse<Any>? = ApiResponse(ApiResponse.Status.LOADING, null, null)

        if (!NetworkUtil.isNetworkAvailable(this)) {
            return
        }

        val mLogin = restApiFactory!!.create()
            .logout(
                auth,
                contentTypeJson,
                lang = SharedPreferenceUtility.getInstance().get(Constant.SELECTED_LANGUAGE)
            )
        mLogin.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CommonModel> {

                override fun onSubscribe(d: Disposable) {
                    responseLiveData.postValue(apiResponse!!.loading())
                }

                override fun onNext(result: CommonModel) {
                    SharedPreferenceUtility.getInstance().save(Constant.IS_LOGIN, false)
                    // SharedPreferenceUtility.getInstance().clearSharedPreferences()
                    SharedPreferenceUtility.getInstance().save(Constant.LOGGED_IN_AUTH_TOKEN, null)
                    SharedPreferenceUtility.getInstance().save(Constant.LOGGED_IN_EMAIL, null)
                    SharedPreferenceUtility.getInstance().save(Constant.LOGGED_IN_MOBILE, null)
                    SharedPreferenceUtility.getInstance().save(Constant.LOGGED_IN_USER_ID, null)
                    SharedPreferenceUtility.getInstance().save(Constant.LOGGED_IN_USER_IMAGE, null)
                    LoginActivity.startActivity(this@BaseActivity, null)
                    finishAffinity()

                    //responseLiveData.postValue(apiResponse!!.success(result))
                }

                override fun onError(throwable: Throwable) {
                    responseLiveData.postValue(apiResponse!!.error(throwable))
                }

                override fun onComplete() {}
            })*/

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
    @Override
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
                finish()
            }
        }
    }



    protected fun showSnackBar(message: String?) {
        Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
    }

    private val view: View
        private get() = findViewById(R.id.content)


}