package com.gorych.debts.core.watcher

import android.text.Editable
import android.text.TextWatcher

/**
 * The goal of this class to provide possibility to override only single action
 * without boilerplate 'no-op' functions
 */
abstract class AbstractOnTextChangedWatcher : TextWatcher {
    override fun afterTextChanged(text: Editable?) {
        //no-op
    }

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        //no-op
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        //no-op
    }
}