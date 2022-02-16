package com.kuo.bookkeeping.ui.bookkeeping.save_record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.kuo.bookkeeping.data.Result
import com.kuo.bookkeeping.data.Result.*
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.di.AppModule.DefaultDispatcher
import com.kuo.bookkeeping.domain.consumption.*
import com.kuo.bookkeeping.domain.consumption.ConsumptionError.*
import com.kuo.bookkeeping.util.Event
import com.kuo.bookkeeping.util.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.ParseException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SaveRecordViewModel @Inject constructor(
    private val insertOrUpdateConsumptionUseCase: InsertOrUpdateConsumptionUseCase,
    private val convertYearMonthDayToTimestampUseCase: ConvertYearMonthDayToTimestampUseCase,
    private val convertStringToTimestampUseCase: ConvertStringToTimestampUseCase,
    private val convertTimestampToStringUseCase: ConvertTimestampToStringUseCase,
    private val convertAmountInputToValueUseCase: ConvertAmountInputToValueUseCase,
    private val formatAmountValueUseCase: FormatAmountValueUseCase,
    private val validateConsumptionFieldUseCase: ValidateConsumptionFieldUseCase,
    private val savedStateHandle: SavedStateHandle,
    getConsumptionDetailUseCase: GetConsumptionDetailUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var currentId: Int = 0
    private var currentAmount: Float? = null
    private var currentCategory: Category? = null
    private var currentDate: Long = Calendar.getInstance().timeInMillis
    private var currentRemark: String? = null

    private val _uiState = MutableStateFlow(SaveRecordUiState())
    val uiState: StateFlow<SaveRecordUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            savedStateHandle.getLiveData<Int>(CONSUMPTION_ID_KEY)
                .asFlow()
                .filterNot { it < 0 }
                .collect { id ->
                    val result = getConsumptionDetailUseCase(id)
                    handleResult(result)
                }
        }
    }

    private fun handleResult(result: Result<ConsumptionDetail>) {
        if (result is Success) {
            setDetailData(result.data)
        } else if (result is Error) {
            handleResultError(result.exception)
        }
    }

    private fun setDetailData(detail: ConsumptionDetail) {
        currentId = detail.consumptionId
        currentAmount = detail.amount
        currentCategory = Category(
            categoryId = detail.categoryId,
            categoryName = detail.categoryName,
            groupId = 0
        )
        try {
            currentDate = convertStringToTimestampUseCase(detail.date)
        } catch (e: ParseException) {

        }
        currentRemark = detail.remark

        _uiState.update { currentState ->
            val formatAmountText = formatAmountValueUseCase(currentAmount)
            currentState.copy(
                amount = formatAmountText,
                categoryName = currentCategory?.categoryName,
                date = convertTimestampToStringUseCase(currentDate),
                remark = currentRemark
            )
        }
    }

    private fun handleResultError(error: Exception) {

    }

    fun saveRecord() {
        viewModelScope.launch(defaultDispatcher) {
            try {
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

    fun setAmount(amount: CharSequence?) {
        viewModelScope.launch(defaultDispatcher) {
            currentAmount = convertAmountInputToValueUseCase(amount)
            _uiState.update { currentState ->
                val formatText = formatAmountValueUseCase(currentAmount)
                currentState.copy(amount = formatText)
            }
        }
    }

    fun setRemark(remark: CharSequence?) {
        viewModelScope.launch(defaultDispatcher) {
            currentRemark = remark?.toString()
            _uiState.update { currentState ->
                currentState.copy(remark = currentRemark)
            }
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
            consumptionId = currentId,
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
                    currentUiState.copy(date = convertTimestampToStringUseCase(currentDate))
                }
            }
        }
    }

    fun userMessageShown(messageId: Long) {
        viewModelScope.launch(defaultDispatcher) {
            _uiState.update { currentUiState ->
                val messages = currentUiState.userMessages.filterNot { it.id == messageId }
                currentUiState.copy(userMessages = messages)
            }
        }
    }

    fun setId(id: Int) {
        savedStateHandle[CONSUMPTION_ID_KEY] = id
    }

    companion object {
        private const val CONSUMPTION_ID_KEY = "consumption_id_key"
    }
}

data class SaveRecordUiState(
    val amount: String? = null,
    val categoryName: String? = null,
    val date: String? = null,
    val remark: String? = null,
    val isSaveSuccess: Event<Boolean> = Event(false),
    val userMessages: List<UserMessage<ConsumptionError>> = emptyList(),
    val isResetFields: Event<Boolean> = Event(false)
)

class InvalidConsumptionFieldException(
    val invalidFields: List<InvalidField>
) : Exception()