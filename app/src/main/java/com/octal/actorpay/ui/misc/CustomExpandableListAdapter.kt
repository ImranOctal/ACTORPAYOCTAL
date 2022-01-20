package com.octal.actorpay.ui.misc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.octal.actorpay.R
import com.octal.actorpay.repositories.retrofitrepository.models.misc.FAQResponseData


class CustomExpandableListAdapter internal constructor(
    private val context: Context,
    private val list: List<FAQResponseData>,
) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return list[listPosition].answer
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item_child_faq, null)
        }
        val expandedListTextView = convertView!!.findViewById<WebView>(R.id.listView)
        expandedListTextView.loadDataWithBaseURL("",expandedListText,"text/html","UTF-8","")
        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        return 1
    }
    override fun getGroup(listPosition: Int): Any {
        return this.list[listPosition].question
    }
    override fun getGroupCount(): Int {
        return this.list.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item_faq, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listView)
        listTitleTextView.text = listTitle
        return convertView
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }




}