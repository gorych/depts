package com.gorych.debts.receipt.repository

import android.util.Log
import com.gorych.debts.debt.ui.add.AddDebtActivity
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.receipt.Receipt
import com.gorych.debts.receipt.dao.ReceiptDao
import java.util.UUID

class ReceiptRepository(private val receiptDao: ReceiptDao) {

    suspend fun add(purchaserId: UUID, seller: String?) {
        try {
            receiptDao.add(Receipt(purchaserId, seller))
        } catch (e: Exception) {
            Log.e(
                TAG, "Error while adding new receipt to DB. Purchaser id=${purchaserId}", e
            )
        }
    }

    suspend fun getActiveDebtsOfPurchaser(purchaser: Purchaser): List<Receipt> {
        return receiptDao.findOpenByPurchaserId(purchaser.id)
    }

    suspend fun getAllDebtsOfPurchaser(purchaser: Purchaser): List<Receipt> {
        return receiptDao.findAllByPurchaserId(purchaser.id)
    }

    companion object {
        private val TAG = AddDebtActivity::class.simpleName
    }
}