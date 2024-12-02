package com.gorych.debts.purchaser

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "purchaser")
data class Purchaser(
    @PrimaryKey val id: UUID,
    @ColumnInfo val name: String,
    @ColumnInfo val surname: String,
    @ColumnInfo val phoneNumber: String?
) : Parcelable {
    constructor(name: String, surname: String, phoneNumber: String?) : this(
        UUID.randomUUID(),
        name,
        surname,
        phoneNumber
    )

    fun fullName(): String = "$surname $name"

    //TODO
    fun hasActiveDebts(): Boolean = true
}
