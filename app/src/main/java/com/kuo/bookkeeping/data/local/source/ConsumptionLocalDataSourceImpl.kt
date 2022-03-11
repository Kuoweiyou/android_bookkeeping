package com.kuo.bookkeeping.data.local.source

import androidx.paging.PagingSource
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.dao.ConsumptionDao
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptionsPagingSource
import com.kuo.bookkeeping.util.DataNotFoundException
import com.kuo.bookkeeping.util.NoDataDeleteException

class ConsumptionLocalDataSourceImpl(
    private val consumptionDao: ConsumptionDao
) : ConsumptionLocalDataSource {

    override suspend fun insert(consumption: Consumption): Result<Long> {
        return try {
            Success(consumptionDao.insert(consumption))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun update(consumption: Consumption): Result<Int> {
        return try {
            Success(consumptionDao.update(consumption))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override fun getConsumptionsGroupByDate(): PagingSource<Int, DayConsumptions> {
        return DayConsumptionsPagingSource(consumptionDao)
    }

    override suspend fun getConsumptionDetailById(id: Int): Result<ConsumptionDetail> {
        return try {
            val data = consumptionDao.getConsumptionDetailById(id)
            if (data == null) {
                throw DataNotFoundException("The id: $id of consumption is not found")
            } else {
                Success(data)
            }
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun delete(id: Int): Result<Int> {
        return try {
            val rowCount = consumptionDao.delete(id)
            if (rowCount == 0) {
                throw NoDataDeleteException("The id: $id of consumption is not delete")
            } else {
                Success(rowCount)
            }
        } catch (e: Exception) {
            Error(e)
        }
    }
}