package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption

interface UpdateConsumptionUseCase {

    suspend operator fun invoke(consumption: Consumption): Result<Int>
}