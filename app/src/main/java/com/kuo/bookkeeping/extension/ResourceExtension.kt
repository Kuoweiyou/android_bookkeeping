package com.kuo.bookkeeping.extension

import androidx.annotation.StringRes
import com.kuo.bookkeeping.MyApplication

// issue: 加上 vararg 標記參數會使結果不正確
fun getString(@StringRes resId: Int, formatArg: Any? = null): String {
    return MyApplication.instance.getString(resId, formatArg)
}