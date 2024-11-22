package com.gorych.debts.core.activity

import androidx.appcompat.app.AppCompatActivity
import com.gorych.debts.R
import com.gorych.debts.core.fragment.TopBarFragment

abstract class TopBarActivityBase : AppCompatActivity() {

    fun initTopBarFragment(titleResourceId: Int, iconResourceId: Int) {
        initTopBarFragment(
            getString(titleResourceId),
            iconResourceId
        )
    }

    fun initTopBarFragment(title: String, iconResourceId: Int) {
        val fragment = TopBarFragment.newInstance(
            title,
            iconResourceId
        )
        supportFragmentManager.beginTransaction()
            .replace(R.id.top_bar_fragment_container, fragment)
            .commit()
    }

}