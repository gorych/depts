package com.gorych.debts.core.validation

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.utility.textAsString

class NotBlankTextAndValidLengthValidator(
    private val input: TextInputEditText,
    private val inputLayout: TextInputLayout,
    context: Context,
) : EditTextValidatorBase(input, inputLayout, context, R.string.blank_text_or_wrong_length) {

    override fun validCondition(): Boolean {
        val text = input.textAsString()
        return text.isNotBlank()
                && text.length <= inputLayout.counterMaxLength
    }
}