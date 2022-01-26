package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.domain.consumption.ConsumptionError.InvalidField
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ValidateConsumptionFieldUseCaseImpl(
    private val defaultDispatcher: CoroutineDispatcher
) : ValidateConsumptionFieldUseCase {

    override suspend fun invoke(
        amount: Float?,
        categoryId: Int?
    ): List<InvalidField> = withContext(defaultDispatcher) {
        val result = mutableListOf<InvalidField>()
        when {
            (amount == null) -> result.add(InvalidField.AMOUNT)
            (categoryId == null) -> result.add(InvalidField.CATEGORY_ID)
        }
        result.toList()
    }
}