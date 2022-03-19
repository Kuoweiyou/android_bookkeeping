package com.kuo.bookkeeping.ui.bookkeeping.save_record

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.text.format.DateUtils
import android.view.Gravity
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import com.kuo.bookkeeping.MainActivity
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.databinding.FragmentSaveRecordBinding
import com.kuo.bookkeeping.extension.themeColor
import com.kuo.bookkeeping.extension.trimEndZero
import com.kuo.bookkeeping.ui.base.BaseFragment
import com.kuo.bookkeeping.ui.bookkeeping.BookkeepingGraphViewModel
import com.kuo.bookkeeping.ui.bookkeeping.REFRESH_KEY
import com.kuo.bookkeeping.util.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Expression
import java.text.DecimalFormat
import java.util.*
import kotlin.NumberFormatException

@AndroidEntryPoint
class SaveRecordFragment : BaseFragment<FragmentSaveRecordBinding>(
    FragmentSaveRecordBinding::inflate
), DatePickerDialog.OnDateSetListener {

    private val viewModel: SaveRecordViewModel by viewModels()
    private val graphViewModel: BookkeepingGraphViewModel by hiltNavGraphViewModels(R.id.graph_bookkeeping)
    private val args: SaveRecordFragmentArgs by navArgs()

    private var tempCategory: Category? = null
        set(value) {
            field = value
            binding.tvCategoryName.text = field?.categoryName ?: getString(R.string.content_description_choose_category)
        }
    private var tempCalendar = Calendar.getInstance()
        set(value) {
            field = value
            setDateText(field)
        }

    private fun setDateText(calendar: Calendar) {
        binding.tvDate.text = if (DateUtils.isToday(calendar.timeInMillis)) {
            getString(R.string.today)
        } else {
            getString(
                R.string.choose_date_format,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    override fun setupView(view: View) {
        setupTransition()
    }

    private fun setupTransition() {
        val isFromDetailPage = args.consumptionId != -1
        if (isFromDetailPage) {
            enterTransition = Slide().apply {
                slideEdge = Gravity.END
                duration = resources.getInteger(R.integer.motion_duration).toLong()
                addTarget(binding.root)
            }
            returnTransition = Slide().apply {
                slideEdge = Gravity.END
                duration = 150.toLong()
                addTarget(binding.root)
            }
        } else {
            enterTransition = MaterialContainerTransform().apply {
                startView = requireActivity().findViewById(R.id.fab_add_record)
                endView = binding.root
                duration = resources.getInteger(R.integer.motion_duration).toLong()
                scrimColor = Color.TRANSPARENT
                containerColor = requireContext().themeColor(R.attr.colorSurface)
                startContainerColor = requireContext().themeColor(R.attr.colorAccent)
                endContainerColor = requireContext().themeColor(R.attr.colorSurface)
                addTarget(binding.root)
            }
            returnTransition = Slide().apply {
                duration = 225L
                addTarget(binding.root)
            }
        }
    }

    override fun setupListener() {
        binding.tvAmount.setOnClickListener {
            if (!binding.keyboard.isVisible) {
                showKeyboard()
            }
        }
        setupKeyboard()
        binding.btnChooseCategory.setOnClickListener {
            showCategoryListDialog()
        }
        binding.btnChooseDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnSave.setOnClickListener {
            val amount = try {
                binding.tvAmount.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                // set 0 to text, and hint user is 0

                setDefaultAmount()
                0f
            }
            println("date: day: ${tempCalendar.get(Calendar.DAY_OF_MONTH)}, time: ${tempCalendar.timeInMillis}")
            viewModel.saveRecord(
                amount = amount,
                categoryId = tempCategory?.categoryId,
                date = tempCalendar,
                remark = binding.edtRemark.text.toString()
            )
        }
    }

    private fun showKeyboard() {
        val transition = androidx.transition.Slide(Gravity.BOTTOM).apply {
            duration = resources.getInteger(R.integer.motion_duration).toLong()
            addTarget(binding.keyboard)
        }
        TransitionManager.beginDelayedTransition(binding.root, transition)
        binding.keyboard.visibility = View.VISIBLE

        (requireActivity() as MainActivity).hideNavBar()
    }

    private fun setupKeyboard() {
        binding.keyboard.binding.btn0.setOnClickListener {
            binding.tvAmount.text = addToInputText("0")
        }
        binding.keyboard.binding.btn1.setOnClickListener {
            binding.tvAmount.text = addToInputText("1")
        }
        binding.keyboard.binding.btn2.setOnClickListener {
            binding.tvAmount.text = addToInputText("2")
        }
        binding.keyboard.binding.btn3.setOnClickListener {
            binding.tvAmount.text = addToInputText("3")
        }
        binding.keyboard.binding.btn4.setOnClickListener {
            binding.tvAmount.text = addToInputText("4")
        }
        binding.keyboard.binding.btn5.setOnClickListener {
            binding.tvAmount.text = addToInputText("5")
        }
        binding.keyboard.binding.btn6.setOnClickListener {
            binding.tvAmount.text = addToInputText("6")
        }
        binding.keyboard.binding.btn7.setOnClickListener {
            binding.tvAmount.text = addToInputText("7")
        }
        binding.keyboard.binding.btn8.setOnClickListener {
            binding.tvAmount.text = addToInputText("8")
        }
        binding.keyboard.binding.btn9.setOnClickListener {
            binding.tvAmount.text = addToInputText("9")
        }
        binding.keyboard.binding.btnDot.setOnClickListener {
            binding.tvAmount.text = addToInputText(".")
        }
        binding.keyboard.binding.btnAddition.setOnClickListener {
            binding.tvAmount.text = addToInputText("+")
        }
        binding.keyboard.binding.btnSubtraction.setOnClickListener {
            binding.tvAmount.text = addToInputText("-")
        }
        binding.keyboard.binding.btnMultiply.setOnClickListener {
            binding.tvAmount.text = addToInputText("×")
        }
        binding.keyboard.binding.btnDivision.setOnClickListener {
            binding.tvAmount.text = addToInputText("÷")
        }
        binding.keyboard.binding.btnBracketLeft.setOnClickListener {
            binding.tvAmount.text = addToInputText("(")
        }
        binding.keyboard.binding.btnBracketRight.setOnClickListener {
            binding.tvAmount.text = addToInputText(")")
        }
        binding.keyboard.binding.btnBack.setOnClickListener {
            val amountText = binding.tvAmount.text
            if (amountText.length <= 1) {
                binding.tvAmount.text = DEFAULT_AMOUNT
            } else {
                binding.tvAmount.text = amountText.dropLast(1)
            }
        }
        binding.keyboard.binding.btnClear.setOnClickListener {
            binding.tvAmount.text = DEFAULT_AMOUNT
        }
        binding.keyboard.binding.btnEquals.setOnClickListener {
            calculateResult()
        }
    }

    private fun calculateResult() {
        try {
            val expression = getInputExpression()
            val result = Expression(expression).calculate()
            if (result.isNaN()) {
                throw Exception()
            }
            binding.tvAmount.text = DecimalFormat("0.######").format(result).toString()
        } catch (e: Exception) {
            setDefaultAmount()
        }
    }

    private fun setDefaultAmount() {
        binding.tvAmount.text = DEFAULT_AMOUNT
    }

    private fun getInputExpression(): String {
        return binding.tvAmount.text
            .replace(Regex("÷"), "/")
            .replace(Regex("×"), "*")
    }

    private fun addToInputText(buttonValue: String): String {
        return if (binding.tvAmount.text == DEFAULT_AMOUNT) {
            buttonValue
        } else {
            "${binding.tvAmount.text}$buttonValue"
        }
    }

    override fun setupObserver() {
        viewModel.setId(args.consumptionId)
        setupCategoryResultObserver()
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
        uiState.detailAmount?.getContentIfNotHandled()?.let {
            binding.tvAmount.text = it.trimEndZero()
        }
        uiState.detailCategory?.getContentIfNotHandled()?.let {
            tempCategory = it
        }
        uiState.detailDate?.getContentIfNotHandled()?.let {
            tempCalendar = it
        }
        uiState.detailRemark?.getContentIfNotHandled()?.let {
            binding.edtRemark.setText(it)
        }
    }

    private fun showUserMessage(uiState: SaveRecordUiState) {
        uiState.userMessage?.getContentIfNotHandled()?.let {
            val hint = when (it.message) {
                ConsumptionError.InvalidCategory -> getString(R.string.hint_invalid_field_category)
                ConsumptionError.SaveError -> getString(R.string.hint_save_error)
            }
            Toast.makeText(
                context,
                hint,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleSaveSuccess(uiState: SaveRecordUiState) {
        uiState.isSaveSuccess?.getContentIfNotHandled()?.let {
            if (it) {
                showSaveSuccessMessage()
                setRefreshState()
            }
        }
        uiState.isModifyDetailSuccess?.getContentIfNotHandled()?.let {
            if (it) {
                graphViewModel.setDetailRefresh()
                setRefreshState()
                findNavController().navigateUp()
            }
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
        uiState.isResetFields?.getContentIfNotHandled()?.let { isResetFields ->
            if (isResetFields) {
                clearFields()
            }
        }
    }

    private fun clearFields() {
        setDefaultAmount()
        binding.edtRemark.text.clear()
        tempCategory = null
        tempCalendar = Calendar.getInstance()
    }

    private fun showCategoryListDialog() {
        findNavController().navigate(R.id.action_addRecordFragment_to_categoryListDialogFragment)
    }

    private fun showDatePickerDialog() {
        val year = tempCalendar.get(Calendar.YEAR)
        val month = tempCalendar.get(Calendar.MONTH)
        val day = tempCalendar.get(Calendar.DAY_OF_MONTH)

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
                    tempCategory = it
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
        tempCalendar.apply {
            set(year, month, day)
        }
        setDateText(tempCalendar)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.keyboard.isVisible) {
                        hideKeyboard()
                        (requireActivity() as MainActivity).showNavBar()
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })
    }

    private fun hideKeyboard() {
        val transition = androidx.transition.Slide(Gravity.BOTTOM).apply {
            duration = resources.getInteger(R.integer.motion_duration).toLong()
            addTarget(binding.keyboard)
        }
        TransitionManager.beginDelayedTransition(binding.root, transition)
        binding.keyboard.visibility = View.GONE
    }

    companion object {
        const val DEFAULT_AMOUNT = "0"
    }
}

const val CATEGORY_RESULT_KEY = "category_result_key"