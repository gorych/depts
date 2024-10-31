package com.gorych.debts.utility

import android.view.View
import android.widget.TextView

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun TextView.textAsString(): String {
    return text.toString()
}