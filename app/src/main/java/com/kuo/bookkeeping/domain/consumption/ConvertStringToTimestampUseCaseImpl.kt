package com.kuo.bookkeeping.domain.consumption

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.text.ParseException

class ConvertStringToTimestampUseCaseImpl : ConvertStringToTimestampUseCase {

    @Throws(ParseException::class)
    @SuppressLint("SimpleDateFormat")
    override fun invoke(date: String): Long {
        return SimpleDateFormat("yyyy-MM-dd").parse(date).time
    }
}