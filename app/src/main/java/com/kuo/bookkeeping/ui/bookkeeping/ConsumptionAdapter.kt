package com.kuo.bookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.ConsumptionCategoryTuple
import com.kuo.bookkeeping.databinding.ItemConsumptionBinding

class ConsumptionAdapter(
    private val dataSet: List<ConsumptionCategoryTuple>,
    private val onClick: (Int, View) -> Unit
) : RecyclerView.Adapter<ConsumptionViewHolder>() {

    override fun onBindViewHolder(holder: ConsumptionViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionViewHolder {
        return ConsumptionViewHolder.create(parent, onClick)
    }

    override fun getItemCount(): Int = dataSet.size
}

class ConsumptionViewHolder(
    private val binding: ItemConsumptionBinding,
    private val onClick: (Int, View) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ConsumptionCategoryTuple) {
        binding.tvCategoryName.apply {
            text = item.categoryName
            val imageId = context.resources.getIdentifier(
                item.iconName, "drawable", context.packageName
            )
            val image = ContextCompat.getDrawable(context, imageId)
            setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
        }
        binding.tvAmount.text = item.amount.toString()
        binding.root.transitionName = itemView.context.getString(
            R.string.transition_card_consumption_item, item.consumptionId.toString()
        )
        setOnClickListener(item)
    }

    private fun setOnClickListener(item: ConsumptionCategoryTuple) {
        itemView.setOnClickListener {
            onClick.invoke(item.consumptionId, itemView)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClick: (Int, View) -> Unit
        ): ConsumptionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_consumption, parent, false)
            val binding = ItemConsumptionBinding.bind(view)
            return ConsumptionViewHolder(binding, onClick)
        }
    }
}