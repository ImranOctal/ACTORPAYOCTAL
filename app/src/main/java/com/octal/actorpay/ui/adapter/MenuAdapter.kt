package com.octal.actorpay.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.BaseAdapter
import nl.psdcompany.duonavigationdrawer.views.DuoOptionView
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.octal.actorpay.R
import com.octal.actorpay.ui.dashboard.models.DrawerItems

import androidx.core.content.res.ResourcesCompat





class MenuAdapter : BaseAdapter  {
    private var mOptions = ArrayList<DrawerItems>()
    private var mOptionViews = ArrayList<DuoOptionView>()
    var mContext: Context? = null
    constructor(context: Context, options: ArrayList<DrawerItems>) {
        mOptions = options
        this.mContext = context 
    }

    override fun getCount(): Int {
        return mOptions.size
    }

    override fun getItem(position: Int): Any {
        return mOptions.get(position)
    }

    fun setViewSelected(position: Int, selected: Boolean) {
        // Looping through the options in the menu
        // Selecting the chosen option
        for (i in 0 until mOptionViews.size) {
            if (i == position) {
                mOptionViews[i].isSelected = selected
            } else {
                mOptionViews[i].isSelected = !selected
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val option = mOptions.get(position)

        // Using the DuoOptionView to easily recreate the demo
        val optionView: DuoOptionView
        if (convertView == null) {
            optionView = DuoOptionView(parent.context)
        } else {
            optionView = convertView as DuoOptionView
        }

        val textView =(((optionView.getChildAt(0) as RelativeLayout).getChildAt(1) as RelativeLayout).getChildAt(1) as TextView)
        textView.setTextColor(ContextCompat.getColor(mContext!!, R.color.black
           // Color.parseColor("#8E8D8D")
        ))
        val typeface = ResourcesCompat.getFont(mContext!!, R.font.poppins_regular)

        textView.setTypeface(typeface)
        textView.textSize = 17F

        // Using the DuoOptionView's default selectors
        optionView.bind(option.mTitle, option.mDrawable)

        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView)

        return optionView
    }
}