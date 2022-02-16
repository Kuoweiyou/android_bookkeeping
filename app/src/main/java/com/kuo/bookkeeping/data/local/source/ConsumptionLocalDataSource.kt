package com.kuo.bookkeeping.data.local.source

import androidx.paging.PagingSource
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions

interface ConsumptionLocalDataSource {

    suspend fun insertOrUpdate(consumption: Consumption): Result<Long>

    fun getConsumptionsGroupByDate(): PagingSource<Int, DayConsumptions>

    suspend fun getConsumptionDetailById(id: Int): Result<ConsumptionDetail>

    suspend fun delete(id: Int): Result<Int>
}