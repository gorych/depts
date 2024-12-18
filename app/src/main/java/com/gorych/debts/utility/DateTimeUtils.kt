package com.gorych.debts.utility

import java.time.format.DateTimeFormatter

object DateTimeUtils {

    val DD_MM_YYYY_HH_MM_SS_SSS_FORMATER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
}
