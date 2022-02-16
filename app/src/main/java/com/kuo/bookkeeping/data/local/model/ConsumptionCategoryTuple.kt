package com.kuo.bookkeeping.data.local.model

data class ConsumptionCategoryTuple(
    val consumptionId: Int,
    val amount: Float,
    val categoryName: String,
    val iconName: String
)

data class ConsumptionDetail(
    val consumptionId: Int,
    val amount: Float,
    val categoryId: Int,
    val categoryName: String,
    val iconName: String,
    val date: String,
    val remark: String?
)