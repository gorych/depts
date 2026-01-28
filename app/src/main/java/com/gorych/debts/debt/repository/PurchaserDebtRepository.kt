package com.gorych.debts.debt.repository

import com.gorych.debts.debt.Debt
import com.gorych.debts.debt.Debt.Status
import com.gorych.debts.good.Good
import com.gorych.debts.purchaser.Purchaser
import java.time.LocalDateTime.now

class PurchaserDebtRepository {

    fun getActiveDebtsOfPurchaser(purchaser: Purchaser): List<Debt> {
        return getAllDebtsOfPurchaser(purchaser).filter { it.status == Status.OPEN }
    }

    fun getAllDebtsOfPurchaser(purchaser: Purchaser): List<Debt> {
        val goods = listOf(
            Good(1, "Колбаса", "123456789", Good.MeasurementUnit.GRAM),
            Good(2, null.toString(), "987525543", Good.MeasurementUnit.QUANTITY)
        )

        val debts = listOf(
            Debt(1, "Чек №101", now(), Status.OPEN, purchaser, goods, "Алла"),
            Debt(2, "Чек №233", now().minusDays(1), Status.CLOSED, purchaser, goods, "Наташа"),
            Debt(3, "Чек №244", now().minusDays(2), Status.OPEN, purchaser, goods, "Ольга"),
            Debt(4, "Чек №245", now().minusDays(3), Status.CLOSED, purchaser, goods, "Наташа"),
            Debt(5, "Чек №301", now().minusDays(4), Status.OPEN, purchaser, goods, "Наташа"),
            Debt(6, "Чек №308", now().minusDays(5), Status.CLOSED, purchaser, goods, "Наташа"),
            Debt(7, "Чек №443", now().minusDays(6), Status.OPEN, purchaser, goods, "Ольга"),
            Debt(8, "Чек №456", now().minusDays(7), Status.CLOSED, purchaser, goods, "Виктория"),
            Debt(9, "Чек №789", now().minusDays(8), Status.OPEN, purchaser, goods, "Алла"),
            Debt(10, "Чек №901", now().minusDays(9), Status.CLOSED, purchaser, goods, "Алла"),
        )
        return debts
    }

}