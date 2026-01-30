package com.gorych.debts.purchaser.contract

import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.receipt.Receipt

interface PurchaserDetailContract {

    interface View {
        fun populateDebts(receipts: List<Receipt>)
        fun populatePersonalInfo(purchaser: Purchaser)
    }

    interface Presenter {
        suspend fun loadActiveDebts(purchaser: Purchaser)
        suspend fun loadAllDebts(purchaser: Purchaser)
        suspend fun reloadDebts(purchaser: Purchaser, activeDebtsOnly: Boolean)
    }
}