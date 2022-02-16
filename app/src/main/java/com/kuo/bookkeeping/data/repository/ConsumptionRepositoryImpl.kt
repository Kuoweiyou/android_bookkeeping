package com.kuo.bookkeeping.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.data.local.source.ConsumptionLocalDataSource
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import kotlinx.coroutines.flow.Flow

class ConsumptionRepositoryImpl(
    private val consumptionLocalDataSource: ConsumptionLocalDataSource
) : ConsumptionRepository {

    override suspend fun insertOrUpdateConsumption(consumption: Consumption): Result<Long> {
        return consumptionLocalDataSource.insertOrUpdate(consumption)
    }

    override fun getConsumptionsGroupByDate(pageSize: Int): Flow<PagingData<DayConsumptions>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize
            ),
            pagingSourceFactory = {
                consumptionLocalDataSource.getConsumptionsGroupByDate()
            }
        ).flow

    override suspend fun getConsumptionDetailById(id: Int): Result<ConsumptionDetail> {
        return consumptionLocalDataSource.getConsumptionDetailById(id)
    }

    override suspend fun delete(id: Int): Result<Int> {
        return consumptionLocalDataSource.delete(id)
    }
}