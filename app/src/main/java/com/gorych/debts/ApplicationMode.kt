package com.gorych.debts

import androidx.appcompat.app.AppCompatActivity
import com.gorych.debts.barcode.activity.LiveBarcodeScanningActivity
import com.gorych.debts.purchaser.activity.ShowAllClientsActivity

enum class ApplicationMode(
    val titleResId: Int,
    val subtitleResId: Int,
    val activityClass: Class<out AppCompatActivity>
) {
    SHOW_ALL_CLIENTS(
        R.string.mode_show_all_clients_title,
        R.string.mode_show_all_clients_subtitle,
        ShowAllClientsActivity::class.java
    ),
    BARCODE_LIVE(
        R.string.mode_barcode_live_title,
        R.string.mode_barcode_live_subtitle,
        LiveBarcodeScanningActivity::class.java
    ),
}