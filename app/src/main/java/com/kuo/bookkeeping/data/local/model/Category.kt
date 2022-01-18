package com.kuo.bookkeeping.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "category",
    foreignKeys = [ForeignKey(
        entity = CategoryGroup::class,
        parentColumns = [CategoryGroup.PRIMARY_KEY_COLUMN_NAME],
        childColumns = [Category.FOREIGN_KEY_COLUMN_NAME],
        onDelete = CASCADE
    )]
)
data class Category(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PRIMARY_KEY_COLUMN_NAME)
    val categoryId: Int,

    @ColumnInfo(name = "category_name")
    val categoryName: String,

    @ColumnInfo(name = FOREIGN_KEY_COLUMN_NAME)
    val groupId: Int
) : Parcelable {
    companion object {
        const val PRIMARY_KEY_COLUMN_NAME = "category_id"
        const val FOREIGN_KEY_COLUMN_NAME = "group_id"
    }
}