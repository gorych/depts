package com.gorych.debts.purchaser.validation

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.core.validation.InputValidatorBase

class LastNameValidator(
    input: TextInputEditText,
    inputLayout: TextInputLayout,
    context: Context
) : InputValidatorBase(input, inputLayout, context, R.string.not_valid_last_name)