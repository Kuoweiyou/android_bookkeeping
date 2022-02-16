package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.repository.ConsumptionRepository

class DeleteConsumptionUseCaseImpl(
    private val consumptionRepository: ConsumptionRepository
) : DeleteConsumptionUseCase {

    override suspend fun invoke(id: Int): Result<Boolean> {
        return when (val result = consumptionRepository.delete(id)) {
            is Success -> Success(true)
            is Error -> result
            is Loading -> result
        }
    }
}