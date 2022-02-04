package com.kuo.bookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.ConsumptionCategoryTuple

class ConsumptionAdapter(
    private val dataSet: List<ConsumptionCategoryTuple>
) : RecyclerView.Adapter<ConsumptionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvCategoryName: TextView = view.findViewById(R.id.tv_category_name)
        private val tvAmount: TextView = view.findViewById(R.id.tv_amount)

        fun setItem(item: ConsumptionCategoryTuple) {
            tvCategoryName.text = item.categoryName
            tvAmount.text = item.amount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consumption, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size
}