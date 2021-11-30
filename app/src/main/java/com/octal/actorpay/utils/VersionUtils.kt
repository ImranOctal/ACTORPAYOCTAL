package com.octal.actorpay.utils

import android.os.Build

object VersionUtils {
    val isAtLeastL: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    val isAtJellyBeen: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
}