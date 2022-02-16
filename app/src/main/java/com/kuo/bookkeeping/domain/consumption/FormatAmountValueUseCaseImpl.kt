package com.kuo.bookkeeping.domain.consumption

import java.math.RoundingMode
import java.text.NumberFormat

class FormatAmountValueUseCaseImpl : FormatAmountValueUseCase {

    private val formatter by lazy { NumberFormat.getNumberInstance().apply {
        roundingMode = RoundingMode.DOWN
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }}

    override fun invoke(value: Float?): String? {
        return if (value != null) {
            formatter.format(value)
        } else {
            null
        }
    }
}