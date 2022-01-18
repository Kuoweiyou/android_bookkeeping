package com.kuo.bookkeeping.ui.bookkeeping.add_record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.databinding.DialogFragmentCategoryListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListDialogFragment : DialogFragment(), ExpandableListView.OnChildClickListener {

    private var _binding: DialogFragmentCategoryListBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var categoryListAdapter: CategoryListAdapter
    private val viewModel: CategoryListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listCategory.apply {
            setAdapter(categoryListAdapter)
            setOnChildClickListener(this@CategoryListDialogFragment)
        }
        viewModel.loadCategoryDataSet()
        viewModel.categoryList.observe(viewLifecycleOwner, {
            println("category observe in ui: $it")
            categoryListAdapter.dataSet = it
        })
    }

    override fun onChildClick(
        parent: ExpandableListView?,
        view: View?,
        groupPosition: Int,
        childPosition: Int,
        id: Long
    ): Boolean {
        val categoryGroup = categoryListAdapter.categoryGroups[groupPosition]
        val category = categoryListAdapter.childCategories[groupPosition][childPosition]
        findNavController().previousBackStackEntry?.savedStateHandle?.run {
            set(CATEGORY_GROUP_RESULT_KEY, categoryGroup)
            set(CATEGORY_RESULT_KEY, category)
        }
        dismissAllowingStateLoss()
        return false
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}