package com.gorych.debts.barcode.contract

import com.google.mlkit.vision.barcode.common.Barcode
import com.gorych.debts.good.Good

class BarcodeDetectionContract {

    interface View {
        fun refreshWhenWorkflowStatusIsDetecting()
        fun refreshWhenWorkflowStatusIsConfirming()
        fun refreshWhenWorkflowStatusIsSearching()
        fun refreshWhenWorkflowStatusIsDetected()

        fun processDetectedBarcodes()
        fun showBarcodeDetectionResult(barcodeData: Pair<Barcode, ByteArray>, good: Good?)
    }

    interface Presenter {
        suspend fun processDetectedBarcode(barcodeData: Pair<Barcode, ByteArray>)
    }
}