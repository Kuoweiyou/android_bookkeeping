package com.kuo.bookkeeping.data.repository

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup

interface CategoryRepository {

    suspend fun getAllGroupAndCategories(): Result<Map<CategoryGroup, List<Category>>>
}