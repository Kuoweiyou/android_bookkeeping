package com.kuo.bookkeeping.data.local.source

import androidx.paging.PagingSource
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.dao.ConsumptionDao
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptionsPagingSource

class ConsumptionLocalDataSourceImpl(
    private val consumptionDao: ConsumptionDao
) : ConsumptionLocalDataSource {

    override suspend fun insertOrUpdate(consumption: Consumption): Result<Long> {
        return try {
            Success(consumptionDao.insertOrUpdate(consumption))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override fun getConsumptionsGroupByDate(): PagingSource<Int, DayConsumptions> {
        return DayConsumptionsPagingSource(consumptionDao)
    }
}