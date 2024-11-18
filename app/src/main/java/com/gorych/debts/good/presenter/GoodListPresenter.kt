package com.gorych.debts.good.presenter

import com.gorych.debts.good.contract.GoodListContract
import com.gorych.debts.good.repository.GoodRepository

class GoodListPresenter(
    private val view: GoodListContract.View,
    private val goodRepository: GoodRepository,
) : GoodListContract.Presenter {

    override suspend fun loadInitialList() {
        val items = goodRepository.getFirstBatch(BATCH_SIZE)
        view.populateItems(items)
    }

    companion object {
        const val BATCH_SIZE = 100
    }
}