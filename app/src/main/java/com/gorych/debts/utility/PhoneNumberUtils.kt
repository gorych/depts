package com.gorych.debts.utility

import android.text.Editable

object PhoneNumberUtils {

    private fun removeCharactersExceptDigits(phone: Editable?): String =
        phone.toString().replace("\\D".toRegex(), "")

    /**
     * Formatting phone number to (xx) xxx-xx-xx format
     */
    fun format(phoneText: Editable?): String {
        val phone = removeCharactersExceptDigits(phoneText)
        return when {
            phone.length < 2 -> "(${phone.substring(0)}"
            phone.length < 3 -> "(${phone.substring(0, 2)}) " //+
            phone.length < 5 -> "(${phone.substring(0, 2)}) ${phone.substring(2)}" //+
            phone.length < 6 -> "(${phone.substring(0, 2)}) ${phone.substring(2, 5)}-" //+
            phone.length < 7 -> "(${phone.substring(0, 2)}) ${phone.substring(2, 5)}-${phone.substring(5)}" //+
            phone.length < 8 -> "(${phone.substring(0, 2)}) ${phone.substring(2, 5)}-${phone.substring(5, 7)}-"
            else -> "(${phone.substring(0, 2)}) ${phone.substring(2, 5)}-${phone.substring(5, 7)}-${phone.substring(7)}"
        }
    }
}