package com.gorych.debts.good.exception

import android.database.sqlite.SQLiteConstraintException
import com.gorych.debts.good.Good

class BarcodeUniquenessException(good: Good) : RuntimeException() {
    companion object {
        fun hasBarcodeUniquenessError(constraintException: SQLiteConstraintException): Boolean {
            val errorMsg = constraintException.message!!
            return errorMsg.contains("unique", true)
                    && errorMsg.contains("barcode", true)
        }
    }
}
