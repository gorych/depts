package com.gorych.debts.good.validation.barcode

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.core.validation.ValidLengthValidator

class ValidBarcodeLengthValidator(
    input: TextInputEditText,
    private val inputLayout: TextInputLayout,
    private val context: Context,
) : ValidLengthValidator(input, inputLayout, context) {

    override fun showError() {
        inputLayout.error = context.getString(R.string.good_barcode_length_must_be_correct)
    }
}