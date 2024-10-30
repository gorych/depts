package com.gorych.debts.purchaser.contract

import com.gorych.debts.debt.Debt
import com.gorych.debts.purchaser.Purchaser

interface PurchaserDetailContract {

    interface View {
        fun populateDebts(debts: List<Debt>)
    }

    interface Presenter {
        fun loadActiveDebts(purchaser: Purchaser)
    }
}