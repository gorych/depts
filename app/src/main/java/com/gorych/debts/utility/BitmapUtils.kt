package com.gorych.debts.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.gorych.debts.good.Good
import java.io.ByteArrayOutputStream

object BitmapUtils {

    fun getBitmapAsBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun cropBitmap(
        originalBitmap: Bitmap,
        barcodeBoundingBox: Rect
    ): Bitmap {
        val delta = 20
        val potentialCroppedBitmapHeight = barcodeBoundingBox.height() + delta
        val croppedBitmapHeight =
            when {
                potentialCroppedBitmapHeight <= originalBitmap.height -> potentialCroppedBitmapHeight
                else -> barcodeBoundingBox.height()
            }
        return Bitmap.createBitmap(
            originalBitmap,
            0,
            barcodeBoundingBox.top - delta,
            originalBitmap.width,
            croppedBitmapHeight
        )
    }

    fun convertBytesToBitmap(data: ByteArray?): Bitmap? =
        BitmapFactory.decodeByteArray(
            data,
            0,
            data?.size ?: 0
        )

    fun createBitmapFromGood(good: Good): Bitmap? = convertBytesToBitmap(good.imageData)
}