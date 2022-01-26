package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class ConvertYearMonthDayToTimestampUseCaseImpl(
    private val defaultDispatcher: CoroutineDispatcher
) : ConvertYearMonthDayToTimestampUseCase {

    override suspend fun invoke(year: Int, month: Int, day: Int): Result<Long> =
        withContext(defaultDispatcher) {
            return@withContext try {
                val calendar = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                Success(calendar.timeInMillis)
            } catch (e: Exception) {
                Error(e)
            }
        }
}