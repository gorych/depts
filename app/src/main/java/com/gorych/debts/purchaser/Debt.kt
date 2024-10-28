package com.gorych.debts.purchaser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Debt(
    val id: Long,
    val name: String,
    val created: LocalDate,
    val status: Status,
    val purchaser: Purchaser,
    val goods: List<Good>,
    val seller: String
) : Parcelable

enum class Status {
    OPEN,
    CLOSED
}
