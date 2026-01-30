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

    @Query("SELECT * FROM purchaser ORDER BY surname, name")
    suspend fun findAllSortedBySurnameAndName(): List<Purchaser>

    @Query("SELECT COUNT(*) FROM purchaser")
    suspend fun countAll(): Int

    @Query(SqlQuery.SEARCH)
    suspend fun search(searchText: String): List<Purchaser>

    @Query(SqlQuery.SEARCH_COUNT)
    suspend fun countSearched(searchText: String): Int

    class SqlQuery {
        companion object {
            private const val SEARCH_CONDITION = """
                WHERE 
                    name LIKE '%' || :searchText || '%' 
                    OR surname LIKE '%' || :searchText || '%' 
                    OR phoneNumber LIKE '%' || :searchText || '%'"""

            const val SEARCH = "SELECT * FROM purchaser $SEARCH_CONDITION"

            const val SEARCH_COUNT = "SELECT COUNT(*) FROM purchaser $SEARCH_CONDITION"
        }
    }
}