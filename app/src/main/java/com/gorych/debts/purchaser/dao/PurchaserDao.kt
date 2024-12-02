package com.gorych.debts.purchaser.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gorych.debts.purchaser.Purchaser
import java.util.UUID

@Dao
interface PurchaserDao {

    @Insert
    suspend fun add(entity: Purchaser)

    @Update
    suspend fun update(entity: Purchaser)

    @Delete
    suspend fun delete(entity: Purchaser)

    @Query("SELECT * FROM purchaser WHERE id = :id")
    suspend fun findById(id: UUID): Purchaser?

    @Query("SELECT * FROM purchaser")
    suspend fun findAll(): List<Purchaser>

}