package com.gorych.debts.core.validation

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.utility.textAsString

class EmptyTextOrValidLengthValidator(
    private val input: TextInputEditText,
    private val inputLayout: TextInputLayout,
    context: Context,
) : EditTextValidatorBase(input, inputLayout, context, R.string.wrong_length_text) {

    override fun validCondition(): Boolean {
        val text = input.textAsString()
        return text.isEmpty()
                || text.length <= inputLayout.counterMaxLength
    }
}