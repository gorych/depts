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
        barcodeBoundingBox: Rect,
        imgViewHeight: Int
    ): Bitmap {
        val heightRatio = 1.7f //TODO might differ on different screens
        val croppedImgHeight: Int = Math.round(heightRatio * imgViewHeight)

        val boundingBoxCenterY: Int = Math.round(barcodeBoundingBox.top / 2f)
        val croppedImgRatioY = 1.3f
        val croppedImgTop = Math.round(croppedImgRatioY * (boundingBoxCenterY + imgViewHeight / 2))

        return Bitmap.createBitmap(
            originalBitmap,
            0,
            croppedImgTop,
            originalBitmap.width,
            croppedImgHeight
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