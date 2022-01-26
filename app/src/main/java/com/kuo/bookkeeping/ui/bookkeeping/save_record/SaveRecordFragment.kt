package com.kuo.bookkeeping.ui.bookkeeping.save_record

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.databinding.FragmentSaveRecordBinding
import com.kuo.bookkeeping.domain.consumption.ConsumptionError
import com.kuo.bookkeeping.domain.consumption.ConsumptionError.*
import com.kuo.bookkeeping.ui.base.BaseFragment
import com.kuo.bookkeeping.util.UserMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SaveRecordFragment : BaseFragment<FragmentSaveRecordBinding>(
    FragmentSaveRecordBinding::inflate
), DatePickerDialog.OnDateSetListener {

    private val viewModel: SaveRecordViewModel by viewModels()

    override fun setupView() {

    }

    override fun setupListener() {
        binding.btnChooseCategory.setOnClickListener {
            showCategoryListDialog()
        }
        binding.btnChooseDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnSave.setOnClickListener {
            saveRecord()
        }
    }

    override fun setupObserver() {
        setupCategoryResultObserver()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    setUiState(uiState)
                }
            }
        }
    }

    private fun setUiState(uiState: ConsumptionUiState) {
        setCategoryAndDateText(uiState)
        showUserMessage(uiState)
        showSaveSuccessMessage(uiState)
        resetFields(uiState)
    }

    private fun setCategoryAndDateText(uiState: ConsumptionUiState) {
        val categoryName = uiState.categoryName
            ?: getText(R.string.content_description_choose_category)
        binding.tvCategoryName.text = categoryName

        val date = uiState.date ?: getText(R.string.today)
        binding.tvDate.text = date
    }

    private fun showUserMessage(uiState: ConsumptionUiState) {
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

    private fun showSaveSuccessMessage(uiState: ConsumptionUiState) {
        uiState.isSaveSuccess.getContentIfNotHandled()?.let { isSaveSuccess ->
            if (isSaveSuccess) {
                Toast.makeText(
                    context,
                    getText(R.string.hint_save_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun resetFields(uiState: ConsumptionUiState) {
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

    private fun saveRecord() {
        val amountText = binding.edtAmounts.text.toString()
        val remark = binding.edtRemark.text.toString()
        viewModel.saveRecord(amountText, remark)
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
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.addRecordFragment)
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
}

const val CATEGORY_RESULT_KEY = "category_result_key"