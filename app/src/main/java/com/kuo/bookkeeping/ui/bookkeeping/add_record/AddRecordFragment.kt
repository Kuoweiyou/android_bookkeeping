package com.kuo.bookkeeping.ui.bookkeeping.add_record

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup
import com.kuo.bookkeeping.databinding.FragmentAddRecordBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddRecordFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnChooseCategory.setOnClickListener {
            showCategoryListDialog()
        }
        binding.btnChooseDate.setOnClickListener {
            showDatePickerDialog()
        }
        setupCategoryResultObserver()
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
                && navBackStackEntry.savedStateHandle.contains(CATEGORY_GROUP_RESULT_KEY)
                && navBackStackEntry.savedStateHandle.contains(CATEGORY_RESULT_KEY)
            ) {
                val categoryGroup = navBackStackEntry.savedStateHandle.get<CategoryGroup>(
                    CATEGORY_GROUP_RESULT_KEY)
                val category = navBackStackEntry.savedStateHandle.get<Category>(CATEGORY_RESULT_KEY)
                println("group: ${categoryGroup?.groupName}, category: ${category?.categoryName}")
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
        binding.tvDate.text = getString(R.string.choose_date_format, year, month + 1, day)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

const val CATEGORY_GROUP_RESULT_KEY = "category_group_result_key"
const val CATEGORY_RESULT_KEY = "category_result_key"