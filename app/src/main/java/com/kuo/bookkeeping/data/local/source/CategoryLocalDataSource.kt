package com.kuo.bookkeeping.data.local.source

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup

interface CategoryLocalDataSource {

    suspend fun getAllGroupAndCategories(): Result<Map<CategoryGroup, List<Category>>>
}