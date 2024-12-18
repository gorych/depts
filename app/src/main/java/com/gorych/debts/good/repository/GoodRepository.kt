package com.gorych.debts.good.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.google.mlkit.vision.barcode.common.Barcode
import com.gorych.debts.good.Good
import com.gorych.debts.good.dao.GoodDao
import com.gorych.debts.good.exception.BarcodeUniquenessException

class GoodRepository(private val goodDao: GoodDao) {

    suspend fun getFirstBatch(size: Int): List<Good> {
        return goodDao.findAll()
    }

    suspend fun findByBarcode(barcode: Barcode): Good? {
        return goodDao.findByBarcode(barcode.rawValue ?: "")
    }

    suspend fun add(good: Good) {
        try {
            goodDao.add(good)
        } catch (constraintException: SQLiteConstraintException) {
            if (BarcodeUniquenessException.hasBarcodeUniquenessError(constraintException)) {
                Log.e(
                    TAG,
                    "Barcode uniqueness error while adding good (id=${good.id}, barcode=${good.barcode}) to DB"
                )
                throw BarcodeUniquenessException(good)
            }
        } catch (e: Exception) {
            Log.e(
                TAG, "Error while adding good (id=${good.id}, barcode=${good.barcode}) to DB"
            )
        }
    }

    suspend fun update(updatedGood: Good) {
        goodDao.update(updatedGood)
    }

    suspend fun remove(good: Good) {
        goodDao.remove(good)
    }

    companion object {
        private val TAG = GoodRepository::class.simpleName
    }
}
