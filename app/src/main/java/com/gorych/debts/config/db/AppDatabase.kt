package com.gorych.debts.config.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gorych.debts.config.db.converter.Converters
import com.gorych.debts.good.Good
import com.gorych.debts.good.dao.GoodDao
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.dao.PurchaserDao
import com.gorych.debts.receipt.Receipt
import com.gorych.debts.receipt.dao.ReceiptDao

@Database(
    entities = [Purchaser::class, Good::class, Receipt::class],
    version = 102
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun purchaserDao(): PurchaserDao

    abstract fun goodDao(): GoodDao

    abstract fun receiptDao(): ReceiptDao

    companion object {
        private const val DEBTS_DB_NAME = "debts"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = createDbInstance(context)
                INSTANCE = instance
                instance
            }
        }

        private fun createDbInstance(context: Context): AppDatabase {
            val databaseBuilder = Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java,
                DEBTS_DB_NAME
            )
            return databaseBuilder
                //.addMigrations(migration_100_110)
                .build()
        }
    }
}