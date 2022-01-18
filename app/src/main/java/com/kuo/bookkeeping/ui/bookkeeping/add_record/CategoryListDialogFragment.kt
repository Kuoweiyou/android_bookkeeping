package com.kuo.bookkeeping.ui.bookkeeping.add_record

import android.view.View
import android.widget.ExpandableListView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.databinding.DialogFragmentCategoryListBinding
import com.kuo.bookkeeping.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListDialogFragment : BaseDialogFragment<DialogFragmentCategoryListBinding>(
    DialogFragmentCategoryListBinding::inflate
), ExpandableListView.OnChildClickListener {

    @Inject lateinit var categoryListAdapter: CategoryListAdapter
    private val viewModel: CategoryListViewModel by viewModels()

    override fun setupView() {
        binding.listCategory.setAdapter(categoryListAdapter)
    }

    override fun setupListener() {
        binding.listCategory.setOnChildClickListener(this@CategoryListDialogFragment)
    }

    override fun setupObserver() {
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
}