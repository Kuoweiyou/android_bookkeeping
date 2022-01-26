package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result

interface ConvertYearMonthDayToTimestampUseCase {

    suspend operator fun invoke(year: Int, month: Int, day: Int): Result<Long>
}