package com.gorych.debts.config.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gorych.debts.config.db.converter.Converters
import com.gorych.debts.good.Good
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.dao.PurchaserDao

@Database(entities = [Purchaser::class, Good::class], version = 100)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun purchaserDao(): PurchaserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "debts"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}