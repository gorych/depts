package com.gorych.debts.debt

import android.os.Parcelable
import com.gorych.debts.good.Good
import com.gorych.debts.purchaser.Purchaser
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Debt(
    val id: Long,
    val name: String,
    val created: LocalDateTime,
    val status: Status,
    val purchaser: Purchaser,
    val goods: List<Good>,
    val seller: String
) : Parcelable {

    enum class Status {
        OPEN,
        CLOSED
    }
}
