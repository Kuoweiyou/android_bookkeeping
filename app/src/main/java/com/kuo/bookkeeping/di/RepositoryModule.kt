package com.kuo.bookkeeping.di

import com.kuo.bookkeeping.data.local.source.CategoryLocalDataSource
import com.kuo.bookkeeping.data.repository.CategoryRepository
import com.kuo.bookkeeping.data.repository.CategoryRepositoryImpl
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
}