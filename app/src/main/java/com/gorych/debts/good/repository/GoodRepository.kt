package com.gorych.debts.good.repository

import com.gorych.debts.good.Good
import com.gorych.debts.good.dao.GoodDao

class GoodRepository(private val goodDao: GoodDao) {

    suspend fun getFirstBatch(size: Int): List<Good> {
        return goodDao.findAll()
    }
}
