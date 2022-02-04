package com.kuo.bookkeeping.data.repository

import androidx.paging.PagingData
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import kotlinx.coroutines.flow.Flow

interface ConsumptionRepository {

    suspend fun insertOrUpdateConsumption(consumption: Consumption): Result<Long>

    fun getConsumptionsGroupByDate(pageSize: Int): Flow<PagingData<DayConsumptions>>
}