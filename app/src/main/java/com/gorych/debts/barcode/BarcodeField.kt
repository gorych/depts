package com.gorych.debts.barcode

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Information about a barcode field.  */
@Parcelize
data class BarcodeField(val label: String, val value: String) : Parcelable
