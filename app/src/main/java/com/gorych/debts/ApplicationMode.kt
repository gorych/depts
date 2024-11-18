package com.gorych.debts

import androidx.appcompat.app.AppCompatActivity
import com.gorych.debts.barcode.ui.LiveBarcodeScanningActivity
import com.gorych.debts.good.ui.list.GoodListActivity
import com.gorych.debts.purchaser.ui.add.AddClientActivity
import com.gorych.debts.purchaser.ui.list.ClientListActivity

enum class ApplicationMode(
    val titleResId: Int,
    val subtitleResId: Int,
    val iconResId: Int,
    val activityClass: Class<out AppCompatActivity>
) {
    SHOW_ALL_CLIENTS(
        R.string.mode_show_all_clients_title,
        R.string.mode_show_all_clients_subtitle,
        R.drawable.ic_baseline_people_24,
        ClientListActivity::class.java
    ),
    ADD_NEW_CLIENT(
        R.string.mode_add_new_client_title,
        R.string.mode_add_new_client_subtitle,
        R.drawable.ic_baseline_person_add_24,
        AddClientActivity::class.java
    ),
    SHOW_ALL_GOODS(
        R.string.mode_show_all_goods_title,
        R.string.mode_show_all_goods_subtitle,
        R.drawable.ic_baseline_goods_24,
        GoodListActivity::class.java
    ),
    BARCODE_SCANNING(
        R.string.mode_barcode_scanning_title,
        R.string.mode_barcode_scanning_subtitle,
        R.drawable.ic_baseline_barcode_reader_24,
        LiveBarcodeScanningActivity::class.java
    )
}