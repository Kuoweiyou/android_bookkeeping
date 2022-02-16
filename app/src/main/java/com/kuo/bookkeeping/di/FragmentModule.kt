package com.kuo.bookkeeping.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuo.bookkeeping.ui.bookkeeping.save_record.CategoryListAdapter
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
}

@AssistedFactory
interface LinearLayoutManagerFactory {
    fun create(context: Context): MyLinearLayoutManager
}

class MyLinearLayoutManager @AssistedInject constructor(
    @Assisted context: Context
) : LinearLayoutManager(context)