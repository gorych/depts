package com.gorych.debts.purchaser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Purchaser(
    val id: Long,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val hasActiveDebts: Boolean
) : Parcelable {
    fun fullName(): String = "$name $surname"

    fun details(): String = "Phone: $phoneNumber\nHas active debts: $hasActiveDebts"
}
