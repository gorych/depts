package com.gorych.debts.purchaser.contract

import com.gorych.debts.purchaser.Purchaser

interface PurchaserListContract {

    interface View {
        fun populateItems(purchasers: List<Purchaser>)
        fun removeItem(purchaser: Purchaser)
    }

    interface Presenter {
        suspend fun loadInitialList()
    }
}