package com.kuo.bookkeeping.domain.category

import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup
import java.util.SortedMap

interface GetAllSortedGroupAndCategoriesUseCase {

    suspend operator fun invoke(): Result<SortedMap<CategoryGroup, List<Category>>>
}