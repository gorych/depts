package com.gorych.debts.barcode.presenter

import com.google.mlkit.vision.barcode.common.Barcode
import com.gorych.debts.barcode.contract.BarcodeDetectionContract
import com.gorych.debts.good.Good
import com.gorych.debts.good.repository.GoodRepository

class BarcodePresenter(
    private val view: BarcodeDetectionContract.View,
    private val goodRepository: GoodRepository,
) : BarcodeDetectionContract.Presenter {

    override suspend fun processDetectedBarcode(barcodeData: Pair<Barcode, ByteArray>) {
        val foundGood: Good? = goodRepository.findByBarcode(barcodeData.first)
        view.showBarcodeDetectionResult(barcodeData, foundGood)
    }
}