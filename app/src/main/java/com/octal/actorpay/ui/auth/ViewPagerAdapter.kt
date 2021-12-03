package com.octal.actorpay.ui.auth

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.octal.actorpay.utils.CustomPager

class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val COUNT = 2
    val tab = arrayOf("Login", "Signup")
    private var mCurrentPosition = -1

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = LoginScreenFragment()
            1 -> fragment = SignUpScreenFragment()
        }

        return fragment!!
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tab[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE;
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
            if (position != mCurrentPosition) {
                val fragment = `object` as Fragment
                val pager =  container as CustomPager;
                if (fragment.getView() != null) {
                    mCurrentPosition = position;
                    pager.measureCurrentView(fragment.getView());
                }
            }
        }

}

