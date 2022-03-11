package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.repository.ConsumptionRepository

class UpdateConsumptionUseCaseImpl(
    private val consumptionRepository: ConsumptionRepository
) : UpdateConsumptionUseCase {

    override suspend fun invoke(consumption: Consumption): Result<Int> {
        return consumptionRepository.update(consumption)
    }
}