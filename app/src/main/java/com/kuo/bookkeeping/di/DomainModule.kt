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
    ): InsertConsumptionUseCase {
        return InsertConsumptionUseCaseImpl(
            consumptionRepository, defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateConsumptionUseCase(
        consumptionRepository: ConsumptionRepository
    ): UpdateConsumptionUseCase {
        return UpdateConsumptionUseCaseImpl(consumptionRepository)
    }

    @Provides
    fun provideGetConsumptionsGroupByDateUseCase(
        consumptionRepository: ConsumptionRepository
    ): GetConsumptionsGroupByDateUseCase {
        return GetConsumptionsGroupByDateUseCaseImpl(consumptionRepository)
    }

    @Provides
    fun provideDeleteConsumptionUseCase(
        consumptionRepository: ConsumptionRepository
    ): DeleteConsumptionUseCase {
        return DeleteConsumptionUseCaseImpl(consumptionRepository)
    }

    @Provides
    fun provideGetConsumptionDetailUseCase(
        consumptionRepository: ConsumptionRepository
    ): GetConsumptionDetailUseCase {
        return GetConsumptionDetailUseCaseImpl(consumptionRepository)
    }

    @Provides
    fun provideConvertStringToTimestampUseCase(): ConvertStringToTimestampUseCase {
        return ConvertStringToTimestampUseCaseImpl()
    }
}