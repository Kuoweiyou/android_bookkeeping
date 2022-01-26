package com.kuo.bookkeeping.di

import com.kuo.bookkeeping.data.local.source.CategoryLocalDataSource
import com.kuo.bookkeeping.data.local.source.ConsumptionLocalDataSource
import com.kuo.bookkeeping.data.repository.CategoryRepository
import com.kuo.bookkeeping.data.repository.CategoryRepositoryImpl
import com.kuo.bookkeeping.data.repository.ConsumptionRepository
import com.kuo.bookkeeping.data.repository.ConsumptionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCategoryRepository(
        categoryLocalDataSource: CategoryLocalDataSource
    ): CategoryRepository {
        return CategoryRepositoryImpl(
            categoryLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun provideConsumptionRepository(
        consumptionLocalDataSource: ConsumptionLocalDataSource
    ): ConsumptionRepository {
        return ConsumptionRepositoryImpl(
            consumptionLocalDataSource
        )
    }
}