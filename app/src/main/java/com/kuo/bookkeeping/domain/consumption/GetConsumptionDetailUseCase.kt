package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail

interface GetConsumptionDetailUseCase {

    suspend operator fun invoke(id: Int): Result<ConsumptionDetail>
}