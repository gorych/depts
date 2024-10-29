package com.gorych.debts.purchaser.view

import com.gorych.debts.purchaser.Purchaser

interface PurchaserListView {

    fun populateItems(purchasers: List<Purchaser>)
}