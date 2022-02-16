package com.kuo.bookkeeping.domain.consumption

import androidx.paging.PagingData
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import com.kuo.bookkeeping.data.repository.ConsumptionRepository
import kotlinx.coroutines.flow.Flow

class GetConsumptionsGroupByDateUseCaseImpl(
    private val consumptionRepository: ConsumptionRepository
) : GetConsumptionsGroupByDateUseCase {

    override fun invoke(): Flow<PagingData<DayConsumptions>> {
        return consumptionRepository.getConsumptionsGroupByDate(pageSize = PAGE_SIZE)
    }

    companion object {
        const val PAGE_SIZE = 7
    }
}