package com.kuo.bookkeeping.ui.bookkeeping.save_record

import android.view.View
import android.widget.ExpandableListView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.databinding.DialogFragmentCategoryListBinding
import com.kuo.bookkeeping.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListDialogFragment : BaseDialogFragment<DialogFragmentCategoryListBinding>(
    DialogFragmentCategoryListBinding::inflate
), ExpandableListView.OnChildClickListener {

    private val viewModel: CategoryListViewModel by viewModels()
    @Inject lateinit var categoryListAdapter: CategoryListAdapter

    override fun setupView() {
        binding.listCategory.setAdapter(categoryListAdapter)
    }

    override fun setupListener() {
        binding.listCategory.setOnChildClickListener(this@CategoryListDialogFragment)
    }

    override fun setupObserver() {
        viewModel.loadCategoryDataSet()
        viewModel.categoryList.observe(viewLifecycleOwner, {
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
        val category = categoryListAdapter.childCategories[groupPosition][childPosition]
        setCategoryResult(category)
        dismissAllowingStateLoss()
        return false
    }

    private fun setCategoryResult(category: Category) {
        findNavController().previousBackStackEntry?.savedStateHandle?.run {
            set(CATEGORY_RESULT_KEY, category)
        }
    }
}