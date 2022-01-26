package com.kuo.bookkeeping.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.kuo.bookkeeping.data.local.model.Consumption

@Dao
interface ConsumptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(consumption: Consumption): Long
}