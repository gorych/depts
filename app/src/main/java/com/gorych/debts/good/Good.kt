package com.gorych.debts.good

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gorych.debts.barcode.BarcodeResultCard
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.LocalDateTime.now

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
    @ColumnInfo val updatedAt: LocalDateTime?,
    @ColumnInfo val imageData: ByteArray?
) : Parcelable {
    init {
        //TODO
        //Make first letter of the name capital
        name?.let { itName ->
            name.replaceFirstChar { itName[0].uppercaseChar() }
        }
    }

    constructor(id: Long, name: String?, barcode: String) : this(
        id,
        name,
        barcode,
        now(),
        null,
        null
    )

    private constructor(barcode: String, name: String?, imageData: ByteArray?) : this(
        0,
        name,
        barcode,
        now(),
        null,
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

    companion object {
        fun of(card: BarcodeResultCard, name: String): Good {
            return Good(
                barcode = card.barcodeRawValue,
                name = name,
                imageData = card.imgData
            )
        }
    }
}
