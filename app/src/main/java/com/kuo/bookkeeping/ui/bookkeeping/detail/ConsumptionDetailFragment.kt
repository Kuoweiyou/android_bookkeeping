package com.kuo.bookkeeping.ui.bookkeeping.detail

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.databinding.FragmentConsumptionDetailBinding
import com.kuo.bookkeeping.ui.base.BaseFragment
import com.kuo.bookkeeping.ui.bookkeeping.REFRESH_KEY
import com.kuo.bookkeeping.util.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConsumptionDetailFragment : BaseFragment<FragmentConsumptionDetailBinding>(
    FragmentConsumptionDetailBinding::inflate
) {
    private val args: ConsumptionDetailFragmentArgs by navArgs()
    private val viewModel: ConsumptionDetailViewModel by viewModels()

    override fun setupView() {

    }

    override fun setupListener() {
        binding.btnModify.setOnClickListener {
            navigateToSaveRecordPage()
        }
        binding.btnDelete.setOnClickListener {
            viewModel.delete()
        }
    }

    private fun navigateToSaveRecordPage() {
        val action = ConsumptionDetailFragmentDirections
            .toSaveRecordFragmentAction(args.consumptionId)
        findNavController().navigate(action)
    }

    override fun setupObserver() {
        viewModel.setId(args.consumptionId)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    bindDetailState(uiState.detail)
                    bindDeleteState(uiState.isDeleteSuccess)
                }
            }
        }
    }

    private fun bindDetailState(detail: ConsumptionDetail?) {
        println("de bug: bindDetailState detail: $detail")
        detail?.let {
            binding.tvAmount.text = detail.amount.toString()
            binding.tvCategory.apply {
                text = detail.categoryName
                val imageId = context.resources.getIdentifier(
                    detail.iconName, "drawable", context.packageName
                )
                val image = ContextCompat.getDrawable(context, imageId)
                setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
            }
            binding.tvDate.text = detail.date
            binding.tvRemark.text = detail.remark ?: ""
        }
    }

    private fun bindDeleteState(deleteSuccess: Event<Boolean>) {
        deleteSuccess.getContentIfNotHandled()?.let {
            if (it) {
                showDeleteSuccessMessage()
                setRefreshState()
                navigateToBookkeepingPage()
            }
        }
    }

    private fun showDeleteSuccessMessage() {
        Toast.makeText(
            context,
            getText(R.string.delete_success),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setRefreshState() {
        findNavController().getBackStackEntry(R.id.bookkeepingFragment).savedStateHandle.run {
            set(REFRESH_KEY, Event(true))
        }
    }

    private fun navigateToBookkeepingPage() {
        findNavController().navigate(R.id.action_consumptionDetailFragment_to_bookkeepingFragment)
    }
}