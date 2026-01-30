package com.gorych.debts.receipt.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gorych.debts.receipt.Receipt
import java.util.UUID

@Dao
interface ReceiptDao {

    @Insert
    suspend fun add(entity: Receipt)

    @Update
    suspend fun update(entity: Receipt)

    @Delete
    suspend fun delete(entity: Receipt)

    @Query("SELECT * FROM receipt WHERE id = :id")
    suspend fun findById(id: Int): Receipt?

    @Query("SELECT * FROM receipt ORDER BY createdAt")
    suspend fun findAllSortedByCreatedAt(): List<Receipt>

    @Query("SELECT COUNT(id) FROM receipt WHERE status = 'OPEN'")
    suspend fun countOpen(): Int

    @Query("SELECT * FROM receipt WHERE purchaserUuid = :purchaserId")
    suspend fun findAllByPurchaserId(purchaserId: UUID): List<Receipt>

    @Query("SELECT * FROM receipt WHERE purchaserUuid = :purchaserId AND status = 'OPEN'")
    suspend fun findOpenByPurchaserId(purchaserId: UUID): List<Receipt>
}