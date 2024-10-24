package com.gorych.debts.purchaser.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.R
import com.gorych.debts.purchaser.Purchaser

class ClientDetailedInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_client_detailed_info)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clients_details_recycler_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val purchaser: Purchaser? = intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)

        purchaser?.let {
            findViewById<TextView>(R.id.client_details_view_title).text = it.fullName()
        }
    }
}