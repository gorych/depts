package com.gorych.debts.main

import com.gorych.debts.purchaser.contract.ApplicationModeContract

class ApplicationModePresenter(
    private val view: ApplicationModeContract.View,
) : ApplicationModeContract.Presenter {

    override fun loadModes() {
        view.populateItems(ApplicationMode.entries.toList())
    }
}