package com.kuo.bookkeeping.ui.bookkeeping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.domain.consumption.GetConsumptionsGroupByDateUseCase
import com.kuo.bookkeeping.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookkeepingViewModel @Inject constructor(
    getConsumptionsGroupByDateUseCase: GetConsumptionsGroupByDateUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookkeepingUiState())
    val uiState: StateFlow<BookkeepingUiState> = _uiState.asStateFlow()

    private val pager: Flow<PagingData<DayConsumptions>> =
        getConsumptionsGroupByDateUseCase().cachedIn(viewModelScope)

    init {
        viewModelScope.launch(defaultDispatcher) {
            pager.collectLatest { pagingData ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(daysConsumption = pagingData)
                }
            }
        }
    }

    fun refreshPage() {
        viewModelScope.launch(defaultDispatcher) {
            _uiState.update { currentUiState ->
                currentUiState.copy(isRefresh = Event(true))
            }
        }
    }
}

data class BookkeepingUiState(
    val daysConsumption: PagingData<DayConsumptions> = PagingData.empty(),
    val isRefresh: Event<Boolean> = Event(false)
)