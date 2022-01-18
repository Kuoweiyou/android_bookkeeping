package com.kuo.bookkeeping.data.local.source

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.dao.CategoryDao
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CategoryLocalDataSourceImpl(
    private val categoryDao: CategoryDao,
    private val ioDispatcher: CoroutineDispatcher
) : CategoryLocalDataSource {

    override suspend fun getAllGroupAndCategories(): Result<Map<CategoryGroup, List<Category>>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(categoryDao.getAllGroupAndCategories())
            } catch (e: Exception) {
                Error(e)
            }
        }
}