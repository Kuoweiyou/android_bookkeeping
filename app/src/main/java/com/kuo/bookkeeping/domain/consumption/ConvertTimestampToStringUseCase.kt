package com.kuo.bookkeeping.domain.consumption

interface ConvertTimestampToStringUseCase {

    operator fun invoke(timestamp: Long): String
}