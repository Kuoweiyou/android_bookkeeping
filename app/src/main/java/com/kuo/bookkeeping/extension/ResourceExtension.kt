package com.kuo.bookkeeping.extension

import androidx.annotation.StringRes
import com.kuo.bookkeeping.MyApplication

fun getString(@StringRes resId: Int): String {
    return MyApplication.instance.getString(resId)
}