package com.kuo.bookkeeping.di

import androidx.fragment.app.Fragment
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
}