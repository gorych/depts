package com.gorych.debts.good.contract

import com.gorych.debts.good.Good

class SelectableGoodListContract {

    interface View {
        fun populateItems(goods: List<Good>, count: Int)
    }

    interface Presenter {
        suspend fun loadInitialList()

        suspend fun reloadListOnSearch(barcode: String)
    }
}