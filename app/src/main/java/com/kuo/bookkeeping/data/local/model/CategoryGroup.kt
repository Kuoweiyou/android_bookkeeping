package com.kuo.bookkeeping.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_group")
data class CategoryGroup(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PRIMARY_KEY_COLUMN_NAME)
    val groupId: Int,

    @ColumnInfo(name = "icon_name")
    val iconName: String?,

    @ColumnInfo(name = "group_name")
    val groupName: String
) {
    companion object {
        const val PRIMARY_KEY_COLUMN_NAME = "group_id"
    }
}