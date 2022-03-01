package com.kuo.bookkeeping.ui.bookkeeping.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.domain.consumption.DeleteConsumptionUseCase
import com.kuo.bookkeeping.domain.consumption.GetConsumptionDetailUseCase
import com.kuo.bookkeeping.util.DataNotFoundException
import com.kuo.bookkeeping.util.Event
import com.kuo.bookkeeping.util.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsumptionDetailViewModel @Inject constructor(
    private val getConsumptionDetailUseCase: GetConsumptionDetailUseCase,
    private val deleteConsumptionUseCase: DeleteConsumptionUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val detailId = MutableStateFlow(-1)

    private val _uiState = MutableStateFlow(ConsumptionDetailUiState())
    val uiState: StateFlow<ConsumptionDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            detailId
                .filterNot { it < 0 }
                .collect { id ->
                    val result = getConsumptionDetailUseCase(id)
                    handleResult(result)
                }
        }
    }

    private fun handleResult(result: Result<ConsumptionDetail>) {
        if (result is Success) {
            _uiState.update { currentState ->
                currentState.copy(detail = result.data)
            }
        } else if (result is Error) {
            handleError(result.exception)
        }
    }

    private fun handleError(error: Exception) {
        if (error is DataNotFoundException) {
            _uiState.update { currentState ->
                currentState.copy(
                    userMessage = UserMessage(message = DetailError.DATA_NOT_FOUND)
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    userMessage = UserMessage(message = DetailError.IO_ERROR)
                )
            }
        }
    }

    fun setId(id: Int) {
        viewModelScope.launch(defaultDispatcher) {
            detailId.emit(id)
        }
    }

    fun delete() {
        viewModelScope.launch(defaultDispatcher) {
            val id = detailId.value
            val result = deleteConsumptionUseCase(id)
            if (result is Success) {
                _uiState.update { currentState ->
                    currentState.copy(isDeleteSuccess = Event(true))
                }
            } else if (result is Error) {
                _uiState.update { currentState ->
                    currentState.copy(
                        userMessage = UserMessage(message = DetailError.NO_DATA_DELETE)
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(defaultDispatcher) {
            val id = detailId.value
            val result = getConsumptionDetailUseCase(id)
            handleResult(result)
        }
    }
}

data class ConsumptionDetailUiState(
    val detail: ConsumptionDetail? = null,
    val userMessage: UserMessage<DetailError>? = null,
    val isDeleteSuccess: Event<Boolean> = Event(false)
)

enum class DetailError {
    DATA_NOT_FOUND,
    IO_ERROR,
    NO_DATA_DELETE
}