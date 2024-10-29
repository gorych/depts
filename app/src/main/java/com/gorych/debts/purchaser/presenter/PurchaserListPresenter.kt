package com.gorych.debts.purchaser.presenter

import com.gorych.debts.purchaser.repository.PurchaserRepository
import com.gorych.debts.purchaser.view.PurchaserListView

class PurchaserListPresenter(
    private val view: PurchaserListView,
    private val purchaserRepository: PurchaserRepository = PurchaserRepository(),
) {
    fun loadItems() {
        val items = purchaserRepository.getFirstBatch(BATCH_SIZE)
        view.populateItems(items)
    }

    companion object {
        const val BATCH_SIZE = 100
    }
}