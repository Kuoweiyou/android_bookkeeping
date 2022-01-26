package com.kuo.bookkeeping.ui.bookkeeping.save_record

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.data.local.model.Category
import com.kuo.bookkeeping.data.local.model.CategoryGroup
import java.util.*

class CategoryListAdapter(private val context: Context) : BaseExpandableListAdapter() {

    var dataSet: SortedMap<CategoryGroup, List<Category>> = TreeMap()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val categoryGroups: List<CategoryGroup>
        get() {
            return dataSet.keys.toList()
        }

    val childCategories: List<List<Category>>
        get() {
            return dataSet.values.toList()
        }

    override fun getGroupCount(): Int {
        return categoryGroups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childCategories[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return categoryGroups[groupPosition].groupName
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childCategories[groupPosition][childPosition].categoryName
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (groupPosition * 100 + childPosition).toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    @SuppressLint("InflateParams")
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = convertView ?:
            LayoutInflater.from(context).inflate(R.layout.item_group_category, null)
        val textView = view.findViewById<TextView>(R.id.tv_category_group_name)
        textView.apply {
            val categoryGroup = categoryGroups[groupPosition]
            text = categoryGroup.groupName
            val imageId = context.resources.getIdentifier(
                categoryGroup.iconName, "drawable", context.packageName
            )
            val image = ContextCompat.getDrawable(context, imageId)
            setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
        }
        return view
    }

    @SuppressLint("InflateParams")
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = convertView ?:
            LayoutInflater.from(context).inflate(R.layout.item_category, null)
        val textView = view.findViewById<TextView>(R.id.tv_category_name)
        textView.apply {
            val category = childCategories[groupPosition][childPosition]
            text = category.categoryName
        }
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}