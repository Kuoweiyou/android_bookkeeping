package com.kuo.bookkeeping.data.local.dao

import androidx.room.*
import com.kuo.bookkeeping.data.local.model.Consumption
import com.kuo.bookkeeping.data.local.model.ConsumptionCategoryTuple

@Dao
interface ConsumptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(consumption: Consumption): Long

    @MapInfo(keyColumn = "date")
    @Query(
        "SELECT DATE(time/1000, 'unixepoch') AS date, consumption_id AS consumptionId, amount, category_name AS categoryName, icon_name AS iconName " +
        "FROM consumption " +
        "JOIN category ON category.category_id = consumption.category_id " +
        "JOIN category_group ON category_group.group_id = category.group_id " +
        "WHERE date IN (SELECT DATE(time/1000, 'unixepoch') AS date FROM consumption GROUP BY date ORDER BY date DESC LIMIT :start, :limit) " +
        "ORDER BY time DESC"
    )
    suspend fun getConsumptionsGroupByDate(start: Int, limit: Int): Map<String, List<ConsumptionCategoryTuple>>
}