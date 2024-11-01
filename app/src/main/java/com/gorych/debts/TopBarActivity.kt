package com.gorych.debts

import androidx.appcompat.app.AppCompatActivity

abstract class TopBarActivity : AppCompatActivity() {

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