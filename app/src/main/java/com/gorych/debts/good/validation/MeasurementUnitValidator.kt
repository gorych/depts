package com.gorych.debts.good.validation

import android.content.Context
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.core.validation.EditTextValidatorBase
import com.gorych.debts.utility.textAsString

class MeasurementUnitValidator(
    private val input: AutoCompleteTextView,
    inputLayout: TextInputLayout,
    context: Context,
) : EditTextValidatorBase(input, inputLayout, context, R.string.not_selected_unit_of_measurement) {

    override fun condition(): Boolean {
        return input.textAsString().isNotEmpty()
    }
}