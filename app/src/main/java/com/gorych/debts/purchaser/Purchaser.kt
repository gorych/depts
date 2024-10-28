package com.gorych.debts.purchaser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Purchaser(
    val id: Long,
    val name: String,
    val surname: String,
    val phoneNumber: String
) : Parcelable {
    fun fullName(): String = "$name $surname"

    //TODO
    fun hasActiveDebts(): Boolean = true
}
