package com.gorych.debts.home

import com.gorych.debts.ApplicationMode

interface ApplicationModeContract {

    interface View {
        fun populateItems(modes: List<ApplicationMode>)
    }

    interface Presenter {
        fun loadModes()
    }
}