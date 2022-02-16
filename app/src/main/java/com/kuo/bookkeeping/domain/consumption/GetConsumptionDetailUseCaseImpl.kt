package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.data.repository.ConsumptionRepository

class GetConsumptionDetailUseCaseImpl(
    private val consumptionRepository: ConsumptionRepository
) : GetConsumptionDetailUseCase {

    override suspend fun invoke(id: Int): Result<ConsumptionDetail> {
        return consumptionRepository.getConsumptionDetailById(id)
    }
}