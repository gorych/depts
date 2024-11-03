package com.gorych.debts.main

import androidx.appcompat.app.AppCompatActivity
import com.gorych.debts.R
import com.gorych.debts.barcode.activity.LiveBarcodeScanningActivity
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
        ClientListActivity::class.java
    ),
    GOOD_SEARCH(
        R.string.mode_good_search_title,
        R.string.mode_good_search_subtitle,
        R.drawable.ic_baseline_good_search_24,
        LiveBarcodeScanningActivity::class.java
    )
}