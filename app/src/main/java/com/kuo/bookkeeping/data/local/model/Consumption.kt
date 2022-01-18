package com.kuo.bookkeeping.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.SET_NULL
import androidx.room.PrimaryKey

@Entity(
    tableName = "consumption",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = [Category.PRIMARY_KEY_COLUMN_NAME],
        childColumns = [Consumption.FOREIGN_KEY_COLUMN_NAME],
        onDelete = SET_NULL
    )]
)
data class Consumption(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "consumption_id")
    val consumptionId: Int,

    @ColumnInfo(name = "amount")
    val amount: Long,

    @ColumnInfo(name = FOREIGN_KEY_COLUMN_NAME)
    val categoryId: Int?,

    @ColumnInfo(name = "time")
    val time: Long,

    @ColumnInfo(name = "remark")
    val remark: String?
) {
    companion object {
        const val FOREIGN_KEY_COLUMN_NAME = "category_id"
    }
}