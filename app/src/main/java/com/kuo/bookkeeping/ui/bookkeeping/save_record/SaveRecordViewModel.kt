package com.kuo.bookkeeping.ui.bookkeeping.save_record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.domain.consumption.*
import com.kuo.bookkeeping.util.Event
import com.kuo.bookkeeping.util.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SaveRecordViewModel @Inject constructor(
    private val insertConsumptionUseCase: InsertConsumptionUseCase,
    private val updateConsumptionUseCase: UpdateConsumptionUseCase,
    private val convertStringToTimestampUseCase: ConvertStringToTimestampUseCase,
    getConsumptionDetailUseCase: GetConsumptionDetailUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaveRecordUiState())
    val uiState: StateFlow<SaveRecordUiState> = _uiState.asStateFlow()

    private val detailId = MutableStateFlow(DEFAULT_DETAIL_ID)
    private val isFromDetailPage: Boolean
        get() {
            return detailId.value != DEFAULT_DETAIL_ID
        }

    init {
        viewModelScope.launch(defaultDispatcher) {
            detailId
                .filterNot { it < 0 }
                .collect { id ->
                    val result = getConsumptionDetailUseCase(id)
                    handleDetailResult(result)
                }
        }
    }

    private fun handleDetailResult(result: Result<ConsumptionDetail>) {
        if (result is Success) {
            setDetailData(result.data)
        } else if (result is Error) {
            handleDetailResultError(result.exception)
        }
    }

    private fun setDetailData(detail: ConsumptionDetail) {
        _uiState.update { currentUiState ->
            val category = Category(
                categoryId = detail.categoryId,
                categoryName = detail.categoryName,
                groupId = -1
            )
            val timestamp = convertStringToTimestampUseCase(detail.date) // 開新的 get detail with timestamp 就不用再轉換
            currentUiState.copy(
                detailAmount = Event(detail.amount.toString()),
                detailCategory = Event(category),
                detailDate = Event(Calendar.getInstance().apply { timeInMillis = timestamp }),
                detailRemark = Event(detail.remark)
            )
        }
    }

    private fun handleDetailResultError(error: Exception) {

    }

    fun saveRecord(
        amount: Float,
        categoryId: Int?,
        date: Calendar,
        remark: String?
    ) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                categoryId ?: throw ConsumptionError.InvalidCategory
                val consumption = Consumption(
                    consumptionId = if (isFromDetailPage) detailId.value else 0,
                    amount = amount,
                    categoryId = categoryId,
                    time = date.timeInMillis,
                    remark = remark
                )
                println("date view model: day: ${date.get(Calendar.DAY_OF_MONTH)}, time: ${date.timeInMillis}")
                val result = if (isFromDetailPage) {
                    updateConsumptionUseCase(consumption)
                } else {
                    insertConsumptionUseCase(consumption)
                }
                if (result is Success) {
                    handleInsertSuccess()
                } else if (result is Error) {
                    throw ConsumptionError.SaveError
                }
            } catch (e: ConsumptionError) {
                handleSaveError(e)
            }
        }
    }

    private fun handleInsertSuccess() {
        if (isFromDetailPage) {
            _uiState.update { currentUiState ->
                currentUiState.copy(isModifyDetailSuccess = Event(true))
            }
        } else {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isSaveSuccess = Event(true),
                    isResetFields = Event(true)
                )
            }
        }
    }

    private fun handleSaveError(error: ConsumptionError) {
        val message = UserMessage(message = error)
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessage = Event(message))
        }
    }

    fun setId(id: Int) {
        viewModelScope.launch(defaultDispatcher) {
            detailId.emit(id)
        }
    }

    companion object {
        private const val DEFAULT_DETAIL_ID = -1
    }
}

data class SaveRecordUiState(
    val detailAmount: Event<String>? = null,
    val detailCategory: Event<Category>? = null,
    val detailDate: Event<Calendar>? = null,
    val detailRemark: Event<String?>? = null,
    val isSaveSuccess: Event<Boolean>? = null,
    val userMessage: Event<UserMessage<ConsumptionError>>? = null,
    val isResetFields: Event<Boolean>? = null,
    val isModifyDetailSuccess: Event<Boolean>? = null
)

sealed class ConsumptionError : Exception() {
    object InvalidCategory : ConsumptionError()
    object SaveError : ConsumptionError()
}