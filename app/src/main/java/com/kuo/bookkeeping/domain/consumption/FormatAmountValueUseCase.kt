package com.kuo.bookkeeping.domain.consumption

interface FormatAmountValueUseCase {

    operator fun invoke(value: Float?): String?
}