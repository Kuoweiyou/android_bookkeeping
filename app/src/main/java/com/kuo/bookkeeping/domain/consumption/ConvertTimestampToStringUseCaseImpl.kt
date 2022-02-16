package com.kuo.bookkeeping.domain.consumption

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.*

class ConvertTimestampToStringUseCaseImpl : ConvertTimestampToStringUseCase {

    /**
     * @param timestamp the time in milliseconds
     */
    @SuppressLint("SimpleDateFormat")
    override fun invoke(timestamp: Long): String {
        return SimpleDateFormat("yyyy/MM/dd").format(Date(timestamp))
    }
}