package com.gorych.debts.good.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gorych.debts.good.Good

@Dao
interface GoodDao {

    @Insert
    suspend fun add(good: Good)

    @Update
    suspend fun update(good: Good)

    @Delete
    suspend fun remove(good: Good)

    @Query("SELECT * FROM good WHERE barcode = :barcode")
    suspend fun findByBarcode(barcode: String): Good?

    @Query("SELECT * FROM good WHERE barcode LIKE :barcode")
    suspend fun searchByBarcode(barcode: String): List<Good>

    @Query("SELECT COUNT(id) FROM good WHERE barcode LIKE :barcode")
    suspend fun countByBarcode(barcode: String): Int

    @Query("SELECT * FROM good ORDER BY createdAt DESC, name ASC")
    suspend fun findAll(): List<Good>

    @Query("SELECT COUNT(id) FROM good")
    suspend fun countAll(): Int
}