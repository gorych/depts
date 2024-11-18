package com.gorych.debts.good.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gorych.debts.good.Good

@Dao
interface GoodDao {

    @Insert
    suspend fun add(good: Good)

    @Query("SELECT * FROM good WHERE barcode = :barcode")
    suspend fun findByBarcode(barcode: String): Good?

    @Query("SELECT * FROM good")
    suspend fun findAll(): List<Good>
}