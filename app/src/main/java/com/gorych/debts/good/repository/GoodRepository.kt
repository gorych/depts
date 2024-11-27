package com.gorych.debts.good.repository

import com.google.mlkit.vision.barcode.common.Barcode
import com.gorych.debts.good.Good
import com.gorych.debts.good.dao.GoodDao

class GoodRepository(private val goodDao: GoodDao) {

    suspend fun getFirstBatch(size: Int): List<Good> {
        return goodDao.findAll()
    }

    suspend fun findByBarcode(barcode: Barcode): Good? {
        return goodDao.findByBarcode(barcode.rawValue ?: "")
    }

    suspend fun add(good: Good) {
        goodDao.add(good)
    }

    suspend fun update(updatedGood: Good) {
        goodDao.update(updatedGood)
    }

    suspend fun remove(good: Good) {
        goodDao.remove(good)
    }
}
