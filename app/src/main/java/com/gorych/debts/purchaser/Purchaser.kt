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
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo val name: String,
    @ColumnInfo val surname: String,
    @ColumnInfo val phoneNumber: String?
) : Parcelable {
    fun fullName(): String = "$name $surname"

    //TODO
    fun hasActiveDebts(): Boolean = true
}
