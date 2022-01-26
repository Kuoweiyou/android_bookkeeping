package com.kuo.bookkeeping.data.local.source

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption

interface ConsumptionLocalDataSource {

    suspend fun insertOrUpdateConsumption(consumption: Consumption): Result<Long>
}