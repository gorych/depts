package com.gorych.debts.purchaser.repository

import com.gorych.debts.purchaser.Purchaser

class PurchaserRepository {

    fun getFirstBatch(size: Int): List<Purchaser> {
        val purchasers =
            (1..size).map {
                Purchaser(
                    "$it".toLong(),
                    "Егор$it",
                    "Семенченя$it",
                    if (it % 2 == 0) "+375 25 159470 $it" else null
                )
            }
        return purchasers
    }

}