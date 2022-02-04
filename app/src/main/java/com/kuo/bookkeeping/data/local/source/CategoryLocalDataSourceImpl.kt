package com.kuo.bookkeeping.data.local.source

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.dao.CategoryDao
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup

class CategoryLocalDataSourceImpl(
    private val categoryDao: CategoryDao
) : CategoryLocalDataSource {

    override suspend fun getAllGroupAndCategories(): Result<Map<CategoryGroup, List<Category>>> {
        return try {
            Success(categoryDao.getAllGroupAndCategories())
        } catch (e: Exception) {
            Error(e)
        }
    }
}