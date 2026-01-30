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

    suspend fun countAll(): Int {
        return purchaserDao.countAll()
    }

    suspend fun search(searchText: String): List<Purchaser> {
        return purchaserDao.search(searchText)
    }

    suspend fun countSearched(searchText: String): Int {
        return purchaserDao.countSearched(searchText)
    }
}