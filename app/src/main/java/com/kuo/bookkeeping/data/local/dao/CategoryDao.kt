package com.kuo.bookkeeping.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup

@Dao
interface CategoryDao {

    @Query(
        "SELECT * FROM category_group JOIN category ON category.group_id = category_group.group_id"
    )
    suspend fun getAllGroupAndCategories(): Map<CategoryGroup, List<Category>>
}