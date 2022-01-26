package com.kuo.bookkeeping.ui.bookkeeping.save_record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.domain.consumption.*
import com.kuo.bookkeeping.domain.consumption.ConsumptionError.*
import com.kuo.bookkeeping.util.Event
import com.kuo.bookkeeping.util.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SaveRecordViewModel @Inject constructor(
    private val insertOrUpdateConsumptionUseCase: InsertOrUpdateConsumptionUseCase,
    private val convertYearMonthDayToTimestampUseCase: ConvertYearMonthDayToTimestampUseCase,
    private val formatDateUseCase: FormatDateUseCase,
    private val validateConsumptionFieldUseCase: ValidateConsumptionFieldUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var currentAmount: Float? = null
    private var currentCategory: Category? = null
    private var currentDate: Long = Calendar.getInstance().timeInMillis
    private var currentRemark: String? = null

    private val _uiState = MutableStateFlow(ConsumptionUiState())
    val uiState: StateFlow<ConsumptionUiState> = _uiState.asStateFlow()

    fun saveRecord(amountText: String, remark: String) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                setAmount(amountText)
                setRemark(remark)
                val consumption = validateFields()
                insertOrUpdate(consumption)
                resetFields()
            } catch (e: InvalidConsumptionFieldException) {
                handleInvalidFieldError(e.invalidFields)
            } catch (e: Exception) {
                handleSaveError()
            }
        }
    }

    @Throws(InvalidConsumptionFieldException::class)
    private fun setAmount(amountText: String) {
        try {
            currentAmount = amountText.toFloat()
        } catch (e: NumberFormatException) {
            throw InvalidConsumptionFieldException(listOf(InvalidField.AMOUNT))
        }
    }

    private fun setRemark(remark: String) {
        if (remark.isNotEmpty()) {
            currentRemark = remark
        }
    }

    @Throws(InvalidConsumptionFieldException::class)
    private suspend fun validateFields(): Consumption {
        val categoryId = currentCategory?.categoryId
        val amount = currentAmount
        val invalidFields = validateConsumptionFieldUseCase(amount, categoryId)
        if (invalidFields.isNotEmpty()) {
            throw InvalidConsumptionFieldException(invalidFields)
        }
        return Consumption(
            consumptionId = 0,
            amount = amount!!,
            categoryId = categoryId,
            time = currentDate,
            remark = currentRemark
        )
    }

    @Throws(Exception::class)
    private suspend fun insertOrUpdate(consumption: Consumption) {
        val result = insertOrUpdateConsumptionUseCase(consumption)
        if (result is Success) {
            _uiState.update { currentUiState ->
                currentUiState.copy(isSaveSuccess = Event(true))
            }
        } else if (result is Error) {
            throw result.exception
        }
    }

    private fun resetFields() {
        currentAmount = null
        currentCategory = null
        currentDate = Calendar.getInstance().timeInMillis
        currentRemark = null

        _uiState.update { currentUiState ->
            currentUiState.copy(
                categoryName = null,
                date = null,
                isResetFields = Event(true)
            )
        }
    }

    private fun handleInvalidFieldError(
        invalidFields: List<InvalidField>
    ) {
        val messages = invalidFields.map {
            UserMessage<ConsumptionError>(message = it)
        }
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessages = messages)
        }
    }

    private fun handleSaveError() {
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessages = listOf(UserMessage(message = SaveError)))
        }
    }

    fun setCategory(category: Category) {
        viewModelScope.launch(defaultDispatcher) {
            currentCategory = category
            _uiState.update { currentUiState ->
                currentUiState.copy(categoryName = currentCategory?.categoryName)
            }
        }
    }

    fun setDate(
        year: Int,
        month: Int,
        day: Int
    ) {
        viewModelScope.launch(defaultDispatcher) {
            val timestampResult = convertYearMonthDayToTimestampUseCase(year, month, day)
            if (timestampResult is Success) {
                currentDate = timestampResult.data
                _uiState.update { currentUiState ->
                    val formatDate = formatDateUseCase(year, month, day)
                    currentUiState.copy(date = formatDate)
                }
            }
        }
    }

    fun userMessageShown(messageId: Long) {
        _uiState.update { currentUiState ->
            val messages = currentUiState.userMessages.filterNot { it.id == messageId }
            currentUiState.copy(userMessages = messages)
        }
    }
}

data class ConsumptionUiState(
    val categoryName: String? = null,
    val date: String? = null,
    val isSaveSuccess: Event<Boolean> = Event(false),
    val userMessages: List<UserMessage<ConsumptionError>> = emptyList(),
    val isResetFields: Event<Boolean> = Event(false)
)

class InvalidConsumptionFieldException(
    val invalidFields: List<InvalidField>
) : Exception()