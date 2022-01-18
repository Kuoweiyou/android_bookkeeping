package com.kuo.bookkeeping.data.repository

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.source.CategoryLocalDataSource
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup

class CategoryRepositoryImpl(
    private val categoryLocalDataSource: CategoryLocalDataSource
) : CategoryRepository {

    override suspend fun getAllGroupAndCategories(): Result<Map<CategoryGroup, List<Category>>> {
        return categoryLocalDataSource.getAllGroupAndCategories()
    }
}