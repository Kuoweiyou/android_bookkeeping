package com.kuo.bookkeeping.di

import android.content.Context
import androidx.room.Room
import com.kuo.bookkeeping.data.local.AppDatabase
import com.kuo.bookkeeping.data.local.dao.CategoryDao
import com.kuo.bookkeeping.data.local.dao.ConsumptionDao
import com.kuo.bookkeeping.data.local.source.CategoryLocalDataSource
import com.kuo.bookkeeping.data.local.source.CategoryLocalDataSourceImpl
import com.kuo.bookkeeping.data.local.source.ConsumptionLocalDataSource
import com.kuo.bookkeeping.data.local.source.ConsumptionLocalDataSourceImpl
import com.kuo.bookkeeping.di.AppModule.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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
        categoryDao: CategoryDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CategoryLocalDataSource {
        return CategoryLocalDataSourceImpl(
            categoryDao, ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideConsumptionLocalDataSource(
        consumptionDao: ConsumptionDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ConsumptionLocalDataSource {
        return ConsumptionLocalDataSourceImpl(
            consumptionDao, ioDispatcher
        )
    }
}