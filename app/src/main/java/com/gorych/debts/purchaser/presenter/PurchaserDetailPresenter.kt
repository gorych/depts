package com.gorych.debts.purchaser.presenter

import com.gorych.debts.debt.repository.PurchaserDebtRepository
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserDetailContract

class PurchaserDetailPresenter(
    private val view: PurchaserDetailContract.View,
    private val purchaserDebtRepository: PurchaserDebtRepository,
) : PurchaserDetailContract.Presenter {

    override suspend fun loadActiveDebts(purchaser: Purchaser) {
        val debts = purchaserDebtRepository.getActiveDebtsOfPurchaser(purchaser)
        view.populateDebts(debts)
    }

    override suspend fun loadAllDebts(purchaser: Purchaser) {
        val debts = purchaserDebtRepository.getAllDebtsOfPurchaser(purchaser)
        view.populateDebts(debts)
    }

    override suspend fun reloadDebts(purchaser: Purchaser, activeDebtsOnly: Boolean) {
        if (activeDebtsOnly) {
            loadActiveDebts(purchaser)
        } else {
            loadAllDebts(purchaser)
        }
    }

}