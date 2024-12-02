package com.gorych.debts.purchaser.presenter

import com.gorych.debts.purchaser.contract.PurchaserListContract
import com.gorych.debts.purchaser.repository.PurchaserRepository

class PurchaserListPresenter(
    private val view: PurchaserListContract.View,
    private val purchaserRepository: PurchaserRepository,
) : PurchaserListContract.Presenter {

    override suspend fun loadInitialList() {
        val items = purchaserRepository.getFirstBatch(BATCH_SIZE)
        view.populateItems(items)
    }

    companion object {
        const val BATCH_SIZE = 100
    }
}