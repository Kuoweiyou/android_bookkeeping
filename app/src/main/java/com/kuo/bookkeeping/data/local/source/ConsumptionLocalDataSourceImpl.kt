package com.kuo.bookkeeping.data.local.source

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.dao.ConsumptionDao
import com.kuo.bookkeeping.data.local.model.Consumption
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ConsumptionLocalDataSourceImpl(
    private val consumptionDao: ConsumptionDao,
    private val ioDispatcher: CoroutineDispatcher
) : ConsumptionLocalDataSource {

    override suspend fun insertOrUpdateConsumption(consumption: Consumption): Result<Long> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(consumptionDao.insertOrUpdate(consumption))
            } catch (e: Exception) {
                Error(e)
            }
        }
}