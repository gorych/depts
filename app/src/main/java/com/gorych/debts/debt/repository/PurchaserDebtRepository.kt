package com.gorych.debts.debt.repository

import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.receipt.Receipt
import com.gorych.debts.receipt.dao.ReceiptDao

class PurchaserDebtRepository(private val receiptDao: ReceiptDao) {

    suspend fun getActiveDebtsOfPurchaser(purchaser: Purchaser): List<Receipt> {
        return receiptDao.findOpenByPurchaserId(purchaser.id)
    }

    suspend fun getAllDebtsOfPurchaser(purchaser: Purchaser): List<Receipt> {
        return receiptDao.findAllByPurchaserId(purchaser.id)
    }

}