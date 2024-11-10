package com.gorych.debts.purchaser.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gorych.debts.purchaser.Purchaser
import java.util.UUID

@Dao
interface PurchaserDao {

    @Insert
    suspend fun insert(entity: Purchaser)

    @Query("SELECT * FROM purchaser WHERE id = :id")
    suspend fun findById(id: UUID): Purchaser?

    @Query("SELECT * FROM purchaser")
    suspend fun findAll(): List<Purchaser>

    @Query("SELECT * FROM purchaser ORDER BY name, surname ASC")
    suspend fun findFirstAndSortByNameAndSurname()

}