package com.kuo.bookkeeping.ui.bookkeeping.add_record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.domain.category.GetAllSortedGroupAndCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val getAllSortedGroupAndCategoriesUseCase: GetAllSortedGroupAndCategoriesUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var job: Job? = null

    private val _categoryList = MutableLiveData<SortedMap<CategoryGroup, List<Category>>>()
    val categoryList: LiveData<SortedMap<CategoryGroup, List<Category>>> = _categoryList

    fun loadCategoryDataSet() {
        job = viewModelScope.launch(defaultDispatcher) {
            val categoryListResult = getAllSortedGroupAndCategoriesUseCase()
            if (categoryListResult is Success) {
                _categoryList.postValue(categoryListResult.data)
                println("category get result after postValue: ${categoryListResult.data}")
            } else if (categoryListResult is Error) {
                println("category result error: ${categoryListResult.exception}")
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}