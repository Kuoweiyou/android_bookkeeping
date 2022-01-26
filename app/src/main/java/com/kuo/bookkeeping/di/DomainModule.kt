package com.kuo.bookkeeping.di

import com.kuo.bookkeeping.data.repository.CategoryRepository
import com.kuo.bookkeeping.data.repository.ConsumptionRepository
import com.kuo.bookkeeping.domain.category.GetAllSortedGroupAndCategoriesUseCase
import com.kuo.bookkeeping.domain.category.GetAllSortedGroupAndCategoriesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.domain.consumption.*

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetAllSortedGroupAndCategoriesUseCase(
        categoryRepository: CategoryRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): GetAllSortedGroupAndCategoriesUseCase {
        return GetAllSortedGroupAndCategoriesUseCaseImpl(
            categoryRepository, defaultDispatcher
        )
    }

    @Provides
    fun provideInsertConsumptionUseCase(
        consumptionRepository: ConsumptionRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): InsertOrUpdateConsumptionUseCase {
        return InsertOrUpdateConsumptionUseCaseImpl(
            consumptionRepository, defaultDispatcher
        )
    }

    @Provides
    fun provideConvertYearMonthDayToTimestampUseCase(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): ConvertYearMonthDayToTimestampUseCase {
        return ConvertYearMonthDayToTimestampUseCaseImpl(
            defaultDispatcher
        )
    }

    @Provides
    fun provideFormatDateUseCase(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): FormatDateUseCase {
        return FormatDateUseCaseImpl(
            defaultDispatcher
        )
    }

    @Provides
    fun provideValidateConsumptionFieldUseCase(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): ValidateConsumptionFieldUseCase {
        return ValidateConsumptionFieldUseCaseImpl(
            defaultDispatcher
        )
    }
}