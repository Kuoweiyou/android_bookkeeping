package com.kuo.bookkeeping.ui.bookkeeping.save_record

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.databinding.FragmentSaveRecordBinding
import com.kuo.bookkeeping.domain.consumption.ConsumptionError
import com.kuo.bookkeeping.domain.consumption.ConsumptionError.*
import com.kuo.bookkeeping.ui.base.BaseFragment
import com.kuo.bookkeeping.ui.bookkeeping.BookkeepingGraphViewModel
import com.kuo.bookkeeping.ui.bookkeeping.REFRESH_KEY
import com.kuo.bookkeeping.util.Event
import com.kuo.bookkeeping.util.UserMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SaveRecordFragment : BaseFragment<FragmentSaveRecordBinding>(
    FragmentSaveRecordBinding::inflate
), DatePickerDialog.OnDateSetListener {

    private val viewModel: SaveRecordViewModel by viewModels()
    private val graphViewModel: BookkeepingGraphViewModel by hiltNavGraphViewModels(R.id.graph_bookkeeping)
    private val args: SaveRecordFragmentArgs by navArgs()

    private val amountTextWatcher: TextWatcher by lazy { initAmountTextWatcher() }
    private val remarkTextWatcher: TextWatcher by lazy { initRemarkTextWatcher() }

    override fun setupView(view: View) {

    }

    override fun setupListener() {
        binding.btnChooseCategory.setOnClickListener {
            showCategoryListDialog()
        }
        binding.btnChooseDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnSave.setOnClickListener {
            viewModel.saveRecord()
        }
        binding.edtAmounts.addTextChangedListener(amountTextWatcher)
        binding.edtRemark.addTextChangedListener(remarkTextWatcher)
    }

    override fun setupObserver() {
        setupCategoryResultObserver()
        viewModel.setId(args.consumptionId)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    bindUiState(uiState)
                }
            }
        }
    }

    private fun bindUiState(uiState: SaveRecordUiState) {
        setFieldsText(uiState)
        showUserMessage(uiState)
        handleSaveSuccess(uiState)
        resetFields(uiState)
    }

    private fun setFieldsText(uiState: SaveRecordUiState) {
        binding.edtAmounts.apply {
            removeTextChangedListener(amountTextWatcher)
            setText(uiState.amount)
            setSelection(uiState.amount?.length ?: 0)
            addTextChangedListener(amountTextWatcher)
        }
        binding.tvCategoryName.text = uiState.categoryName ?: getText(R.string.content_description_choose_category)
        binding.tvDate.text = uiState.date ?: getText(R.string.today)
        binding.edtRemark.apply {
            removeTextChangedListener(remarkTextWatcher)
            setText(uiState.remark)
            setSelection(uiState.remark?.length ?: 0)
            addTextChangedListener(remarkTextWatcher)
        }
    }

    private fun showUserMessage(uiState: SaveRecordUiState) {
        uiState.userMessages.firstOrNull()?.let { userMessage ->
            showUserMessage(userMessage)
        }
    }

    private fun showUserMessage(userMessage: UserMessage<ConsumptionError>) {
        val hint = when (userMessage.message) {
            InvalidField.AMOUNT -> getText(R.string.hint_invalid_field_amount)
            InvalidField.CATEGORY_ID -> getText(R.string.hint_invalid_field_category)
            SaveError -> getText(R.string.hint_save_error)
        }
        Toast.makeText(
            context,
            hint,
            Toast.LENGTH_SHORT
        ).show()

        viewModel.userMessageShown(userMessage.id)
    }

    private fun handleSaveSuccess(uiState: SaveRecordUiState) {
        uiState.isSaveSuccess.getContentIfNotHandled()?.let {
            if (it) {
                showSaveSuccessMessage()
                setRefreshState()
            }
        }
        uiState.isModifyDetailSuccess.getContentIfNotHandled()?.let {
            if (it) { graphViewModel.setDetailRefresh() }
        }
    }

    private fun showSaveSuccessMessage() {
        Toast.makeText(
            context,
            getText(R.string.hint_save_success),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setRefreshState() {
        findNavController().getBackStackEntry(R.id.bookkeepingFragment).savedStateHandle.run {
            set(REFRESH_KEY, Event(true))
        }
    }

    private fun resetFields(uiState: SaveRecordUiState) {
        uiState.isResetFields.getContentIfNotHandled()?.let { isResetFields ->
            if (isResetFields) {
                clearFields()
            }
        }
    }

    private fun clearFields() {
        binding.edtAmounts.text.clear()
        binding.edtRemark.text.clear()
    }

    private fun showCategoryListDialog() {
        findNavController().navigate(R.id.action_addRecordFragment_to_categoryListDialogFragment)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            this,
            year,
            month,
            day
        ).show()
    }

    private fun setupCategoryResultObserver() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.saveRecordFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(CATEGORY_RESULT_KEY)
            ) {
                val category = navBackStackEntry.savedStateHandle.get<Category>(CATEGORY_RESULT_KEY)
                category?.let {
                    viewModel.setCategory(it)
                    navBackStackEntry.savedStateHandle.set(CATEGORY_RESULT_KEY, null)
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    override fun onDateSet(
        picker: DatePicker?,
        year: Int,
        month: Int,
        day: Int
    ) {
        viewModel.setDate(year, month, day)
    }

    private fun initAmountTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setAmount(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }

    private fun initRemarkTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setRemark(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }
}

const val CATEGORY_RESULT_KEY = "category_result_key"