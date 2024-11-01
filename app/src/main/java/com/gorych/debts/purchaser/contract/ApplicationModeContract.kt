package com.gorych.debts.purchaser.contract

import com.gorych.debts.main.ApplicationMode

interface ApplicationModeContract {

    interface View {
        fun populateItems(modes: List<ApplicationMode>)
    }

    interface Presenter {
        fun loadModes()
    }
}