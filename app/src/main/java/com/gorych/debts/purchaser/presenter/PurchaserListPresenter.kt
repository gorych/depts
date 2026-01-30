package com.gorych.debts.purchaser.presenter

import com.gorych.debts.purchaser.contract.PurchaserListContract
import com.gorych.debts.purchaser.repository.PurchaserRepository

class PurchaserListPresenter(
    private val view: PurchaserListContract.View,
    private val purchaserRepository: PurchaserRepository,
) : PurchaserListContract.Presenter {

    override suspend fun loadInitialList() {
        val items = purchaserRepository.getAll()
        val count = purchaserRepository.countAll()
        view.populateItems(items, count)
    }

    override suspend fun reloadListOnSearch(searchText: String) {
        val foundClients = purchaserRepository.search(searchText)
        val count = purchaserRepository.countSearched(searchText)
        view.populateItems(foundClients, count)
    }
}