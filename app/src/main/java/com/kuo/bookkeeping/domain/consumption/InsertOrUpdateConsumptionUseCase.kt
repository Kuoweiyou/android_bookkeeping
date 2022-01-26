package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption

interface InsertOrUpdateConsumptionUseCase {

    suspend operator fun invoke(consumption: Consumption): Result<Long>
}