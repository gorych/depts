package com.gorych.debts.purchaser.repository

import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.dao.PurchaserDao

class PurchaserRepository(purchaserDao: PurchaserDao) {

    fun getFirstBatch(size: Int): List<Purchaser> {
        val purchasers =
            (1..size).map {
                Purchaser(
                    name = "Егор$it",
                    surname = "Семенченя$it",
                    phoneNumber = if (it % 2 == 0) "+375 25 159470 $it" else null
                )
            }
        return purchasers
    }

}