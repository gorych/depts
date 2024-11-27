package com.gorych.debts.good.contract

import com.gorych.debts.good.Good

class GoodListContract {

    interface View {
        fun populateItems(goods: List<Good>)
        fun removeItem(good: Good)
    }

    interface Presenter {
        suspend fun loadInitialList()
    }
}