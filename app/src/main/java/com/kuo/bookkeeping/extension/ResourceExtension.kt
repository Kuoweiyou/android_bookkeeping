package com.kuo.bookkeeping.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.res.use
import com.kuo.bookkeeping.MyApplication

// issue: 加上 vararg 標記參數會使結果不正確
fun getString(@StringRes resId: Int, formatArg: Any? = null): String {
    return MyApplication.instance.getString(resId, formatArg)
}

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}