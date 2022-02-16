package com.kuo.bookkeeping.domain.consumption

class ConvertAmountInputToValueUseCaseImpl : ConvertAmountInputToValueUseCase {

    override fun invoke(input: CharSequence?): Float? {
        return if (input.isNullOrEmpty()) {
            null
        } else {
            try {
                input.toString().filterNot { it == ',' }.toFloat()
            } catch (e: NumberFormatException) {
                throw e
            }
        }
    }
}