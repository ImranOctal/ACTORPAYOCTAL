package com.octal.actorpay.ui.dashboard.`interface`

import android.view.View

interface ItemListenr {
    fun on_ItemClickListner(
        position: Int,
        mList: List<String>,
        view: View
    )
}