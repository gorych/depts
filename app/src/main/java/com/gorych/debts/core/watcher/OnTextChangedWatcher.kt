package com.gorych.debts.core.watcher

import android.text.Editable
import android.text.TextWatcher
import com.gorych.debts.core.validation.TextInputValidator

open class OnTextChangedWatcher(val validator: TextInputValidator) : TextWatcher {
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