package com.gorych.debts.core.activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
        this.findViewById<ConstraintLayout>(R.id.top_bar_fragment_container)
            ?: { Log.e(this.javaClass.simpleName, "Top bar container not found") }

        val fragment = TopBarFragment.newInstance(
            title,
            iconResourceId
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.top_bar_fragment_container, fragment)
            .commit()
    }

}