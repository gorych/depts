package com.gorych.debts.good

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.UUID

@Parcelize
@Entity(tableName = "good")
data class Good(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo val name: String?,
    @ColumnInfo val barcode: String,
    @ColumnInfo val created: LocalDate = LocalDate.now()
) : Parcelable
