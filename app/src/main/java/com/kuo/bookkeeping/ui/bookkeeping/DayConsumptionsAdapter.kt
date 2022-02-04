package com.kuo.bookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.source.paging.DayConsumptions

class DayConsumptionsAdapter(
    private val recycledViewPool: RecyclerView.RecycledViewPool
) : PagingDataAdapter<DayConsumptions, DayConsumptionsViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: DayConsumptionsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setViewPool(recycledViewPool)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayConsumptionsViewHolder {
        return DayConsumptionsViewHolder(parent)
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

class DayConsumptionsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_day_consumptions, parent, false)
) {
    private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    private val rvConsumption: RecyclerView = itemView.findViewById(R.id.rv_consumption)

    fun bind(item: DayConsumptions?) {
        tvDate.text = item?.first
        item?.second?.let {
            rvConsumption.adapter = ConsumptionAdapter(it)
        }
    }

    fun setViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        rvConsumption.setRecycledViewPool(recycledViewPool)
    }
}