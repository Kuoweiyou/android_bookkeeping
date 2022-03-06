package com.kuo.bookkeeping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun setShowAddFab(isShow: Boolean) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isShowAddFab = isShow)
            }
        }
    }
}

data class MainUiState(
    val isShowAddFab: Boolean = false
)