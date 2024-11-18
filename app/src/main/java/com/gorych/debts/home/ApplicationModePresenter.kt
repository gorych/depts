package com.gorych.debts.home

import com.gorych.debts.ApplicationMode

class ApplicationModePresenter(
    private val view: ApplicationModeContract.View,
) : ApplicationModeContract.Presenter {

    override fun loadModes() {
        view.populateItems(ApplicationMode.entries.toList())
    }
}