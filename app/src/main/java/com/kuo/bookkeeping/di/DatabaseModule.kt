package com.kuo.bookkeeping.di

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import com.kuo.bookkeeping.data.local.AppDatabase
import com.kuo.bookkeeping.data.local.dao.CategoryDao
import com.kuo.bookkeeping.data.local.dao.ConsumptionDao
import com.kuo.bookkeeping.data.local.source.CategoryLocalDataSource
import com.kuo.bookkeeping.data.local.source.CategoryLocalDataSourceImpl
import com.kuo.bookkeeping.data.local.source.ConsumptionLocalDataSource
import com.kuo.bookkeeping.data.local.source.ConsumptionLocalDataSourceImpl
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptionsPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_database"
        )
        .createFromAsset("database/app.db")
        .build()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(database: AppDatabase) = database.categoryDao()

    @Singleton
    @Provides
    fun provideConsumptionDao(database: AppDatabase) = database.consumptionDao()

    @Singleton
    @Provides
    fun provideCategoryLocalDataSource(
        categoryDao: CategoryDao
    ): CategoryLocalDataSource {
        return CategoryLocalDataSourceImpl(categoryDao)
    }

    @Singleton
    @Provides
    fun provideDayConsumptionsPagingSource(
        consumptionDao: ConsumptionDao
    ): PagingSource<Int, DayConsumptions> {
        return DayConsumptionsPagingSource(consumptionDao)
    }

    @Singleton
    @Provides
    fun provideConsumptionLocalDataSource(
        consumptionDao: ConsumptionDao
    ): ConsumptionLocalDataSource {
        return ConsumptionLocalDataSourceImpl(
            consumptionDao
        )
    }
}