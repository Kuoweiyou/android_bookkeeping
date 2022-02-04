package com.kuo.bookkeeping.ui.bookkeeping

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.databinding.FragmentBookkeepingBinding
import com.kuo.bookkeeping.ui.base.BaseFragment
import com.kuo.bookkeeping.util.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookkeepingFragment : BaseFragment<FragmentBookkeepingBinding>(
    FragmentBookkeepingBinding::inflate
) {
    private val viewModel: BookkeepingViewModel by viewModels()
    @Inject lateinit var dayConsumptionsAdapter: DayConsumptionsAdapter

    override fun setupView() {
        binding.rvDayConsumption.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dayConsumptionsAdapter
        }
    }

    override fun setupListener() {
        binding.fabAddRecord.setOnClickListener {
            findNavController().navigate(R.id.action_bookkeepingFragment_to_saveRecordFragment)
        }
    }

    override fun setupObserver() {
        setupSaveSuccessResultObserver()
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

    private fun setupSaveSuccessResultObserver() {
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