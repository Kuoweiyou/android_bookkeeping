package com.kuo.bookkeeping.domain.consumption

import com.kuo.bookkeeping.data.Result

interface DeleteConsumptionUseCase {

    suspend operator fun invoke(id: Int): Result<Boolean>
}