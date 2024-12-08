package com.gorych.debts.purchaser.repository

import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.dao.PurchaserDao

class PurchaserRepository(private val purchaserDao: PurchaserDao) {

    suspend fun add(purchaser: Purchaser) {
        purchaserDao.add(purchaser)
    }

    suspend fun remove(purchaser: Purchaser) {
        purchaserDao.delete(purchaser)
    }

    suspend fun getAll(): List<Purchaser> {
        return purchaserDao.findAllSortedBySurnameAndName()
    }
}