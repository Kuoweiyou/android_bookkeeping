package com.kuo.bookkeeping.domain.consumption

interface ConvertStringToTimestampUseCase {

    /**
     * @return the timestamp in milliseconds
     */
    operator fun invoke(date: String): Long
}