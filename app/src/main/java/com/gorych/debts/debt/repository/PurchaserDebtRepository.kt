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
            Debt(1, "Чек №1", now(), Status.OPEN, purchaser, goods, "Алла"),
            Debt(2, "Чек №2", now(), Status.CLOSED, purchaser, goods, "Наташа"),
        )
        return debts
    }

}