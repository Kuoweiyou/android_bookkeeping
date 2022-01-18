package com.kuo.bookkeeping.domain.category

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup
import com.kuo.bookkeeping.data.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class GetAllSortedGroupAndCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : GetAllSortedGroupAndCategoriesUseCase {

    override suspend fun invoke(): Result<SortedMap<CategoryGroup, List<Category>>> =
        withContext(defaultDispatcher) {
            return@withContext try {
                when (val result = categoryRepository.getAllGroupAndCategories()) {
                    is Success -> {
                        val sortedData = sortDataByGroupId(result.data)
                        Success(sortedData)
                    }
                    is Error -> throw result.exception
                    is Loading -> Loading
                }
            } catch (e: Exception) {
                Error(e)
            }
        }

    private fun sortDataByGroupId(
        data: Map<CategoryGroup, List<Category>>
    ) = data.toSortedMap(compareBy { it.groupId })
}