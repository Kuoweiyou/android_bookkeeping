package com.kuo.bookkeeping.ui.bookkeeping

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.databinding.FragmentBookkeepingBinding
import com.kuo.bookkeeping.di.LinearLayoutManagerFactory
import com.kuo.bookkeeping.ui.base.BaseFragment
import com.kuo.bookkeeping.ui.bookkeeping.DayConsumptionsAdapter.DayConsumptionsAdapterFactory
import com.kuo.bookkeeping.util.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookkeepingFragment : BaseFragment<FragmentBookkeepingBinding>(
    FragmentBookkeepingBinding::inflate
) {
    private val viewModel: BookkeepingViewModel by viewModels()

    @Inject lateinit var adapterFactory: DayConsumptionsAdapterFactory
    private lateinit var dayConsumptionsAdapter: DayConsumptionsAdapter
    @Inject lateinit var linearLayoutManagerFactory: LinearLayoutManagerFactory

    override fun setupView() {
        getAdapter()
        binding.rvDayConsumption.apply {
            layoutManager = linearLayoutManagerFactory.create(context)
            adapter = dayConsumptionsAdapter
        }
    }

    private fun getAdapter() {
        dayConsumptionsAdapter = adapterFactory.create(
            onNestedItemClick = { consumptionId ->
                onConsumptionItemClick(consumptionId)
            }
        )
    }

    private fun onConsumptionItemClick(consumptionId: Int) {
        navigateToDetailPage(consumptionId)
    }

    private fun navigateToDetailPage(consumptionId: Int) {
        val action = BookkeepingFragmentDirections
            .toConsumptionDetailFragmentAction(consumptionId)
        findNavController().navigate(action)
    }

    override fun setupListener() {
        binding.fabAddRecord.setOnClickListener {
            findNavController().navigate(R.id.action_bookkeepingFragment_to_saveRecordFragment)
        }
    }

    override fun setupObserver() {
        setupRefreshObserver()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    dayConsumptionsAdapter.submitData(uiState.daysConsumption)
                    handleRefreshState(uiState)
                }
            }
        }
    }

    private fun handleRefreshState(uiState: BookkeepingUiState) {
        uiState.isRefresh.getContentIfNotHandled()?.let {
            if (it) { dayConsumptionsAdapter.refresh() }
        }
    }

    private fun setupRefreshObserver() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.bookkeepingFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(REFRESH_KEY)
            ) {
                val result = navBackStackEntry.savedStateHandle.get<Event<Boolean>>(REFRESH_KEY)
                result?.getContentIfNotHandled()?.let {
                    if (it) { viewModel.refreshPage() }
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
}

const val REFRESH_KEY = "refresh_key"