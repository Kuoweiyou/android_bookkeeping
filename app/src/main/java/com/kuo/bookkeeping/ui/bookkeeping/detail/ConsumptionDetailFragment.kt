package com.kuo.bookkeeping.ui.bookkeeping.detail

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.ConsumptionDetail
import com.kuo.bookkeeping.databinding.FragmentConsumptionDetailBinding
import com.kuo.bookkeeping.ui.base.BaseFragment
import com.kuo.bookkeeping.ui.bookkeeping.BookkeepingGraphViewModel
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
    private val graphViewModel: BookkeepingGraphViewModel by hiltNavGraphViewModels(R.id.graph_bookkeeping)

    override fun setupView(view: View) {
        setupTransition()
    }

    @SuppressLint("StringFormatMatches")
    private fun setupTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.motion_duration).toLong()
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.motion_duration).toLong()
        }
        binding.root.transitionName = getString(
            R.string.transition_card_consumption_item, args.consumptionId.toString()
        )
    }

    override fun setupListener() {
        binding.btnEdit.setOnClickListener {
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                graphViewModel.refreshState.collect { refreshState ->
                    refreshState.isDetailRefresh.getContentIfNotHandled()?.let { isRefresh ->
                        if (isRefresh) { viewModel.refresh() }
                    }
                }
            }
        }
    }

    private fun bindDetailState(detail: ConsumptionDetail?) {
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