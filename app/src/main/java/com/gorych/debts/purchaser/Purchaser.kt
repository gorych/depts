package com.gorych.debts.purchaser

data class Purchaser(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val hasActiveDebts: Boolean
) {
    fun fullName(): String = "$name $surname"

    fun details(): String = "Phone: $phoneNumber\nHas active debts: $hasActiveDebts"
}
