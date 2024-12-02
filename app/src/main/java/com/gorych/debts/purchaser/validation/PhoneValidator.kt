package com.gorych.debts.purchaser.validation

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.core.validation.InputValidatorBase

private const val PHONE_LENGTH_WITHOUT_COUNTRY_CODE = 9

class PhoneValidator(
    private val input: TextInputEditText,
    inputLayout: TextInputLayout,
    context: Context
) : InputValidatorBase(input, inputLayout, context, R.string.not_valid_phone) {

    override fun isValid(): Boolean {
        val text = input.text
        return text.isNullOrEmpty()
                || (text.isNotBlank() && text.length == PHONE_LENGTH_WITHOUT_COUNTRY_CODE)
    }
}