package com.gorych.debts.core.validation

import android.text.Editable
import android.text.TextWatcher

class OnTextChangedWatcher(private val validator: TextInputValidator) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        //no-op
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //no-op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        when {
            !validator.isValid() -> validator.showError()
            else -> validator.clearError()
        }
    }
}