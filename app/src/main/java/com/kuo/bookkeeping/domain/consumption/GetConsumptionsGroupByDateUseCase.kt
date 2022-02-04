package com.kuo.bookkeeping.domain.consumption

import androidx.paging.PagingData
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import kotlinx.coroutines.flow.Flow

interface GetConsumptionsGroupByDateUseCase {

    operator fun invoke(): Flow<PagingData<DayConsumptions>>
}