package com.kuo.bookkeeping.domain.consumption

interface ConvertAmountInputToValueUseCase {

    operator fun invoke(input: CharSequence?): Float?
}