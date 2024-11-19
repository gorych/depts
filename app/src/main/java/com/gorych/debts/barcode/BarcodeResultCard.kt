package com.gorych.debts.barcode

import android.os.Parcelable
import com.google.mlkit.vision.barcode.common.Barcode
import com.gorych.debts.good.Good
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarcodeResultCard(
    val barcodeRawValue: String,
    val associatedGood: Good?,
    val imgData: ByteArray?
) : Parcelable {

    constructor(barcodeRawValue: String, associatedGood: Good?) : this(
        barcodeRawValue, associatedGood, null
    )

    constructor(barcodeRawValue: String, imgData: ByteArray?) : this(
        barcodeRawValue, null, imgData
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BarcodeResultCard

        if (barcodeRawValue != other.barcodeRawValue) return false
        if (associatedGood != other.associatedGood) return false
        if (imgData != null) {
            if (other.imgData == null) return false
            if (!imgData.contentEquals(other.imgData)) return false
        } else if (other.imgData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = barcodeRawValue.hashCode()
        result = 31 * result + (associatedGood?.hashCode() ?: 0)
        result = 31 * result + (imgData?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "BarcodeResultCard(barcodeRawValue='$barcodeRawValue', associatedGood=$associatedGood)"
    }

    companion object {
        fun of(barcodeData: Pair<Barcode, ByteArray>, good: Good?): BarcodeResultCard {
            val barcodeRawValue = barcodeData.first.rawValue ?: ""
            (good?.let {
                return BarcodeResultCard(barcodeRawValue, good)
            }) ?: run {
                return BarcodeResultCard(barcodeRawValue, barcodeData.second)
            }
        }
    }
}
