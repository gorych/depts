package com.gorych.debts.good.ui.add

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gorych.debts.R
import com.gorych.debts.core.activity.TopBarActivityBase

class AddGoodActivity : TopBarActivityBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_good)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initTopBarFragment(
            getString(R.string.add_good_title),
            R.drawable.ic_add_list_24
        )
    }
}