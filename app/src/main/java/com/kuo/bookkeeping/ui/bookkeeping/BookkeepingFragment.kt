package com.kuo.bookkeeping.ui.bookkeeping

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
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

    override fun setupView(view: View) {
        getAdapter()
        binding.rvDayConsumption.apply {
            layoutManager = linearLayoutManagerFactory.create(context)
            adapter = dayConsumptionsAdapter
        }
        setupTransition(view)
    }

    private fun setupTransition(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun getAdapter() {
        dayConsumptionsAdapter = adapterFactory.create(
            onNestedItemClick = { consumptionId, itemView ->
                onConsumptionItemClick(consumptionId, itemView)
            }
        )
    }

    private fun onConsumptionItemClick(consumptionId: Int, itemView: View) {
        navigateToDetailPage(consumptionId, itemView)
    }

    @SuppressLint("StringFormatMatches")
    private fun navigateToDetailPage(consumptionId: Int, itemView: View) {
        val action = BookkeepingFragmentDirections
            .toConsumptionDetailFragmentAction(consumptionId)
        val extras = FragmentNavigatorExtras(
            itemView to getString(R.string.transition_card_consumption_item, consumptionId.toString())
        )
        findNavController().navigate(action, extras)

        exitTransition = null
        reenterTransition = null
    }

    override fun setupListener() {

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