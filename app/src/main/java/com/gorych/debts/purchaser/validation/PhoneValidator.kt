package com.gorych.debts.purchaser.validation

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.core.validation.EditTextValidatorBase

class PhoneValidator(
    private val input: TextInputEditText,
    private val inputLayout: TextInputLayout,
    context: Context,
) : EditTextValidatorBase(input, inputLayout, context, R.string.not_valid_phone) {

    override fun validCondition(): Boolean {
        val text = input.text
        return text.isNullOrEmpty()
                || (text.isNotBlank() && text.length == inputLayout.counterMaxLength)
    }
}