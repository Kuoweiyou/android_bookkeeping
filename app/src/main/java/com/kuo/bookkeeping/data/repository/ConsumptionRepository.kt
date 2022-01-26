package com.kuo.bookkeeping.data.repository

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption

interface ConsumptionRepository {

    suspend fun insertOrUpdateConsumption(consumption: Consumption): Result<Long>
}