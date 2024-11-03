package com.gorych.debts.purchaser.ui.add

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gorych.debts.R
import com.gorych.debts.TopBarActivity

class AddClientActivity : TopBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_purchaser)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initTopBarFragment(getString(R.string.add_client_activity_title), R.drawable.ic_baseline_person_add_24)
    }
}