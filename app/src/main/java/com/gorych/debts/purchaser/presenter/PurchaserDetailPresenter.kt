package com.gorych.debts.purchaser.presenter

import com.gorych.debts.debt.repository.PurchaserDebtRepository
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserDetailContract
import com.gorych.debts.purchaser.repository.PurchaserRepository

class PurchaserDetailPresenter(
    private val view: PurchaserDetailContract.View,
    private val purchaserRepository: PurchaserRepository = PurchaserRepository(),
    private val purchaserDebtRepository: PurchaserDebtRepository = PurchaserDebtRepository(),
) : PurchaserDetailContract.Presenter {

    override fun loadActiveDebts(purchaser: Purchaser) {
        val debts = purchaserDebtRepository.getActiveDebtsOfPurchaser(purchaser)
        view.populateDebts(debts)
    }

}