package com.gorych.debts.good

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(
    tableName = "good",
    indices = [Index(value = ["name"]), Index(value = ["barcode"], unique = true)]
)
data class Good(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val name: String?,
    @ColumnInfo val barcode: String,
    @ColumnInfo val createdAt: LocalDateTime,
    @ColumnInfo val imageData: ByteArray?
) : Parcelable {
    constructor(id: Long, name: String?, barcode: String) : this(
        id,
        name,
        barcode,
        LocalDateTime.now(),
        null
    )

    constructor(barcode: String) : this(
        0,
        null,
        barcode,
        LocalDateTime.now(),
        null
    )

    constructor(barcode: String, name: String?, imageData: ByteArray) : this(
        0,
        name,
        barcode,
        LocalDateTime.now(),
        imageData
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Good

        if (id != other.id) return false
        if (name != other.name) return false
        if (barcode != other.barcode) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + barcode.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "Good(id=$id, name=$name, barcode='$barcode', createdAt=$createdAt)"
    }
}
