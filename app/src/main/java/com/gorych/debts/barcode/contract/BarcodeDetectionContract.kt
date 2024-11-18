package com.gorych.debts.barcode.contract

class BarcodeDetectionContract {

    interface View {
        fun refreshWhenWorkflowStatusIsDetecting()
        fun refreshWhenWorkflowStatusIsConfirming()
        fun refreshWhenWorkflowStatusIsSearching()
        fun refreshWhenWorkflowStatusIsDetected()

        fun processDetectedBarcodes()
    }
}