package com.gorych.debts.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipboardUtils {

    fun copyTextToClipboard(context: Context, label: String, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)
    }
}