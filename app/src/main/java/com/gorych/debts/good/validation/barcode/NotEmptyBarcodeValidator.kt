package com.gorych.debts.good.validation.barcode

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.core.validation.NotEmptyTextValidator

class NotEmptyBarcodeValidator(
    input: TextInputEditText,
    private val inputLayout: TextInputLayout,
    private val context: Context,
) : NotEmptyTextValidator(input, inputLayout, context) {

    override fun showError() {
        inputLayout.error = context.getString(R.string.good_barcode_must_not_be_empty)
    }
}