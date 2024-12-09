package com.gorych.debts.good.validation

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.core.validation.EditTextValidatorBase
import com.gorych.debts.utility.textAsString

class NameValidator(
    private val input: TextInputEditText,
    private val inputLayout: TextInputLayout,
    context: Context,
) : EditTextValidatorBase(input, inputLayout, context, R.string.wrong_length_text) {

    override fun isValid(): Boolean {
        val goodName = input.textAsString()
        return goodName.isEmpty()
                || goodName.length <= inputLayout.counterMaxLength
    }
}