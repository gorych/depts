package com.gorych.debts.purchaser.contract

import com.gorych.debts.debt.Debt
import com.gorych.debts.purchaser.Purchaser

interface PurchaserDetailContract {

    interface View {
        fun populateDebts(debts: List<Debt>)
        fun populatePersonalInfo(purchaser: Purchaser)
    }

    interface Presenter {
        fun loadActiveDebts(purchaser: Purchaser)
        fun loadAllDebts(purchaser: Purchaser)
        fun reloadDebts(purchaser: Purchaser, activeDebtsOnly: Boolean)
    }
}