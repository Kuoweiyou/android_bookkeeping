package com.kuo.bookkeeping.util

import java.util.*

data class UserMessage<T>(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val message: T
)