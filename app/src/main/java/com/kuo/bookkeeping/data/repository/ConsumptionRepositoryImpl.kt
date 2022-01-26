package com.kuo.bookkeeping.data.repository

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.source.ConsumptionLocalDataSource

class ConsumptionRepositoryImpl(
    private val consumptionLocalDataSource: ConsumptionLocalDataSource
) : ConsumptionRepository {

    override suspend fun insertOrUpdateConsumption(consumption: Consumption): Result<Long> {
        return consumptionLocalDataSource.insertOrUpdateConsumption(consumption)
    }
}