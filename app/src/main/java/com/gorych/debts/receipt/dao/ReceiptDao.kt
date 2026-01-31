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

    @Query(SqlQuery.SELECT_BY_PURCHASER_ID_AND_STATUSES)
    suspend fun findByPurchaserIdAndStatusIn(
        purchaserId: UUID,
        receiptStatuses: Set<Receipt.Status>
    ): List<Receipt>

    class SqlQuery {
        companion object {
            const val SELECT_BY_PURCHASER_ID_AND_STATUSES = """
                SELECT * 
                FROM receipt 
                WHERE 
                    purchaserUuid = :purchaserId 
                    AND status IN (:receiptStatuses)"""
        }
    }
}