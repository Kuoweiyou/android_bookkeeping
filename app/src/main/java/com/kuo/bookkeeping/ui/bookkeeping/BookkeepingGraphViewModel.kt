package com.kuo.bookkeeping.ui.bookkeeping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookkeepingGraphViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _refreshState = MutableStateFlow(RefreshState())
    val refreshState: StateFlow<RefreshState> = _refreshState.asStateFlow()

    fun setDetailRefresh() {
        viewModelScope.launch(defaultDispatcher) {
            _refreshState.update { currentState ->
                currentState.copy(isDetailRefresh = Event(true))
            }
        }
    }
}

data class RefreshState(
    val isDetailRefresh: Event<Boolean> = Event(false)
)