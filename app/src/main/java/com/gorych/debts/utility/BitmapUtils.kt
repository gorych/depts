package com.gorych.debts.utility

import android.graphics.Bitmap
import android.graphics.Rect
import java.io.ByteArrayOutputStream

object BitmapUtils {

    fun getBitmapAsBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun cropBitmap(originalBitmap: Bitmap, cropArea: Rect): Bitmap {
        return Bitmap.createBitmap(
            originalBitmap,
            cropArea.left,
            cropArea.top,
            cropArea.width(),
            cropArea.height()
        )
    }
}