package com.kuo.bookkeeping.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuo.bookkeeping.data.local.dao.CategoryDao
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup
import com.kuo.bookkeeping.data.local.model.Consumption

@Database(
    entities = [
        CategoryGroup::class,
        Category::class,
        Consumption::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
}