package com.gorych.debts.good.presenter

import com.gorych.debts.good.contract.GoodListContract
import com.gorych.debts.good.repository.GoodRepository

class GoodListPresenter(
    private val view: GoodListContract.View,
    private val goodRepository: GoodRepository,
) : GoodListContract.Presenter {

    override suspend fun loadInitialList() {
        val items = goodRepository.getAll()
        val count = goodRepository.countAll()
        view.populateItems(items, count)

    }

    override suspend fun reloadListOnSearch(barcode: String) {
        val foundGoods = goodRepository.findByBarcode(barcode)
        val count = goodRepository.countByBarcode(barcode)
        view.populateItems(foundGoods, count)

    }
}