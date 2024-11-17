package com.gorych.debts.good

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "good")
data class Good(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val name: String?,
    @ColumnInfo val barcode: String,
    @ColumnInfo val createdAt: LocalDateTime = LocalDateTime.now()
) : Parcelable
