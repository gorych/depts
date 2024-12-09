package com.gorych.debts.core.validation

interface TextInputValidator {

    fun isActive(): Boolean
    fun isValid(): Boolean
    fun showError()
    fun clearError()
}