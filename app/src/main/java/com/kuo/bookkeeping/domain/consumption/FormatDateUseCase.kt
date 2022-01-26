package com.kuo.bookkeeping.domain.consumption

interface FormatDateUseCase {

    suspend operator fun invoke(year: Int, month: Int, day: Int): String
}