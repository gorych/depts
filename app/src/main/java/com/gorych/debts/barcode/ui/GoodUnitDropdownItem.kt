package com.gorych.debts.barcode.ui

import com.gorych.debts.good.Good.MeasurementUnit

data class GoodUnitDropdownItem(val unit: MeasurementUnit, val textValue: String) {
    override fun toString(): String {
        return textValue
    }
}