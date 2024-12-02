package com.gorych.debts.core.validation

interface TextInputValidator {

    fun isValid(): Boolean
    fun showError()
    fun clearError()
}