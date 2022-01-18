package com.kuo.bookkeeping.di

import com.kuo.bookkeeping.data.repository.CategoryRepository
import com.kuo.bookkeeping.domain.category.GetAllSortedGroupAndCategoriesUseCase
import com.kuo.bookkeeping.domain.category.GetAllSortedGroupAndCategoriesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetAllSortedGroupAndCategoriesUseCase(
        categoryRepository: CategoryRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): GetAllSortedGroupAndCategoriesUseCase {
        return GetAllSortedGroupAndCategoriesUseCaseImpl(
            categoryRepository,
            defaultDispatcher
        )
    }
}