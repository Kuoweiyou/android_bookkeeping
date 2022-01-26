package com.kuo.bookkeeping.domain.consumption

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FormatDateUseCaseImpl(
    private val defaultDispatcher: CoroutineDispatcher
) : FormatDateUseCase {

    override suspend fun invoke(year: Int, month: Int, day: Int): String =
        withContext(defaultDispatcher) {
            String.format("%d/%d/%d", year, month + 1, day)
        }
}