package com.kuo.bookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions
import com.kuo.bookkeeping.databinding.ItemDayConsumptionsBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DayConsumptionsAdapter @AssistedInject constructor(
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    @Assisted private val onNestedItemClick: (Int) -> Unit
) : PagingDataAdapter<DayConsumptions, DayConsumptionsViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: DayConsumptionsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setViewPool(recycledViewPool)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayConsumptionsViewHolder {
        return DayConsumptionsViewHolder.create(parent, onNestedItemClick)
    }

    @AssistedFactory
    interface DayConsumptionsAdapterFactory {
        fun create(onNestedItemClick: (Int) -> Unit): DayConsumptionsAdapter
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<DayConsumptions>() {

            override fun areItemsTheSame(
                oldItem: DayConsumptions,
                newItem: DayConsumptions
            ): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(
                oldItem: DayConsumptions,
                newItem: DayConsumptions
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class DayConsumptionsViewHolder(
    private val binding: ItemDayConsumptionsBinding,
    private val onNestedItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var isAddedItemDecoration = false

    fun bind(item: DayConsumptions?) {
        binding.tvDate.text = item?.first
        item?.second?.let { data ->
            binding.rvConsumption.apply {
                adapter = ConsumptionAdapter(data) { id ->
                    onNestedItemClick.invoke(id)
                }
                if (!isAddedItemDecoration) {
                    addItemDecoration()
                    isAddedItemDecoration = true
                }
            }
        }
    }

    private fun RecyclerView.addItemDecoration() {
        val decoration = DividerItemDecoration(
            context, DividerItemDecoration.VERTICAL
        )
        addItemDecoration(decoration)
    }

    fun setViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        binding.rvConsumption.setRecycledViewPool(recycledViewPool)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onNestedItemLongClick: (Int) -> Unit
        ): DayConsumptionsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_day_consumptions, parent, false)
            val binding = ItemDayConsumptionsBinding.bind(view)
            return DayConsumptionsViewHolder(binding, onNestedItemLongClick)
        }
    }
}