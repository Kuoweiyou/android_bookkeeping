package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.repository.ConsumptionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class InsertConsumptionUseCaseImpl(
    private val consumptionRepository: ConsumptionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : InsertConsumptionUseCase {

    override suspend fun invoke(consumption: Consumption): Result<Long> =
        withContext(defaultDispatcher) {
            return@withContext try {
                when (val result = consumptionRepository.insert(consumption)) {
                    is Success -> result
                    is Error -> throw result.exception
                    is Loading -> Loading
                }
            } catch (e: Exception) {
                Error(e)
            }
        }
}