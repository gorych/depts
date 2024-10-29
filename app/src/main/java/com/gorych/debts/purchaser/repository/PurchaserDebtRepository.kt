package com.gorych.debts.purchaser.repository

import com.gorych.debts.purchaser.Debt
import com.gorych.debts.purchaser.Good
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.Status
import java.time.LocalDate.now

class PurchaserDebtRepository {

    fun getActiveDebtsOfPurchaser(purchaser: Purchaser): List<Debt> {
        return getAllDebtsOfPurchaser(purchaser).filter { it -> it.status == Status.OPEN }
    }

    fun getAllDebtsOfPurchaser(purchaser: Purchaser): List<Debt> {
        val goods = listOf(
            Good(1, "Колбаса", "123456789", now()),
            Good(2, null.toString(), "987525543", now())
        )

        val debts = listOf(
            Debt(1, "Чек №1", now(), Status.OPEN, purchaser, goods, "Алла"),
            Debt(2, "Чек №2", now(), Status.CLOSED, purchaser, goods, "Наташа"),
        )
        return debts
    }

}