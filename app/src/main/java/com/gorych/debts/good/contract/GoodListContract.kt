package com.gorych.debts.good.contract

import com.gorych.debts.good.Good

class GoodListContract {

    interface View {
        fun populateItems(goods: List<Good>, count: Int)
        fun removeItem(good: Good)
    }

    interface Presenter {
        suspend fun loadInitialList()

        suspend fun reloadListOnSearch(barcode: String)
    }
}