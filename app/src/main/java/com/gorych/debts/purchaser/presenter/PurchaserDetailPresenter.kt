package com.gorych.debts.purchaser.presenter

import com.gorych.debts.receipt.repository.ReceiptRepository
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserDetailContract
import com.gorych.debts.receipt.Receipt

class PurchaserDetailPresenter(
    private val view: PurchaserDetailContract.View,
    private val receiptRepository: ReceiptRepository,
) : PurchaserDetailContract.Presenter {

    override suspend fun loadActiveDebts(purchaser: Purchaser) {
        val debts = receiptRepository.getActiveDebtsOfPurchaser(purchaser)
        view.populateDebts(debts)
    }

    override suspend fun loadDebtsWhereStatusIn(purchaser: Purchaser, receiptStatuses: Set<Receipt.Status>) {
        val debts = receiptRepository.getDebtsOfPurchaserWhereStatusIn(purchaser, receiptStatuses)
        view.populateDebts(debts)
    }

    override suspend fun reloadDebts(purchaser: Purchaser, receiptStatuses: Set<Receipt.Status>) {
        if (receiptStatuses.isEmpty()) {
            loadActiveDebts(purchaser)
        } else {
            loadDebtsWhereStatusIn(purchaser, receiptStatuses)
        }
    }

}