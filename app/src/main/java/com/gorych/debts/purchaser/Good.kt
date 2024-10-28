package com.gorych.debts.purchaser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Good(
    val id: Long,
    val name: String,
    val barcode: String,
    val created: LocalDate
) : Parcelable
