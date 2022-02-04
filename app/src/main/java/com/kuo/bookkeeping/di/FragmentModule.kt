package com.kuo.bookkeeping.di

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kuo.bookkeeping.ui.bookkeeping.DayConsumptionsAdapter
import com.kuo.bookkeeping.ui.bookkeeping.save_record.CategoryListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun provideCategoryListAdapter(
        fragment: Fragment
    ): CategoryListAdapter {
        return CategoryListAdapter(fragment.requireContext())
    }

    @Provides
    fun provideRecycledViewPool(): RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    @Provides
    fun provideDaysConsumptionAdapter(
        recycledViewPool: RecyclerView.RecycledViewPool
    ): DayConsumptionsAdapter {
        return DayConsumptionsAdapter(recycledViewPool)
    }
}