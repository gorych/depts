package com.gorych.debts.receipt

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gorych.debts.utility.DateTimeUtils.DD_MM_YYYY_HH_MM_SS_SSS_FORMATER
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID

@Parcelize
@Entity(
    tableName = "receipt",
    indices = [
        Index(value = ["createdAt"]),
        Index(value = ["status"])
    ]
)
data class Receipt(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val createdAt: LocalDateTime,
    @ColumnInfo val updatedAt: LocalDateTime?,
    @ColumnInfo val status: Status,
    @ColumnInfo val purchaserUuid: UUID,
    @ColumnInfo val seller: String?,
) : Parcelable {

    val createdAtFormatted: String
        get() = createdAt.format(DD_MM_YYYY_HH_MM_SS_SSS_FORMATER).toString()

    val updatedAtFormatted: String?
        get() = updatedAt?.format(DD_MM_YYYY_HH_MM_SS_SSS_FORMATER)

    constructor(
        purchaserUuid: UUID,
        seller: String?,
    ) : this(
        id = 0,
        now(),
        null,
        Status.IN_PROGRESS,
        purchaserUuid,
        seller
    )

    enum class Status {
        IN_PROGRESS,
        OPEN,
        CLOSED
    }
}
