package com.gorych.debts.core.validation

import android.content.Context
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

abstract class EditTextValidatorBase(
    private val input: EditText,
    private val inputLayout: TextInputLayout,
    private val context: Context,
    private val errorStringResId: Int,
) : TextInputValidator {

    override fun isValid(): Boolean {
        return !input.text.isNullOrBlank()
    }

    override fun showError() {
        inputLayout.error = context.getString(errorStringResId)
    }

    override fun clearError() {
        inputLayout.error = null
    }
}