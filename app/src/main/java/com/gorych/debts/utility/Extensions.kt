package com.gorych.debts.utility

import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

val String.Companion.EMPTY: String
    get() {
        return ""
    }

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.isGone(): Boolean {
    return visibility == View.GONE
}

fun TextView.textAsString(): String {
    return text.toString()
}

fun TextInputEditText.clearText() {
    text = null
}