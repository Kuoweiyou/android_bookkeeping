package com.kuo.bookkeeping.domain.consumption

interface ValidateConsumptionFieldUseCase {

    suspend operator fun invoke(
        amount: Float?,
        categoryId: Int?
    ): List<ConsumptionError.InvalidField>
}

sealed interface ConsumptionError {

    enum class InvalidField : ConsumptionError {
        AMOUNT,
        CATEGORY_ID
    }

    object SaveError : ConsumptionError
}