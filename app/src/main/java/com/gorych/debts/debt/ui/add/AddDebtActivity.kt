package com.gorych.debts.debt.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gorych.debts.R
import com.gorych.debts.barcode.ui.LiveBarcodeScanningActivity
import com.gorych.debts.core.IntentExtras
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.good.ui.list.selectable.SelectableGoodListActivity
import com.gorych.debts.purchaser.Purchaser

class AddDebtActivity : TopBarActivityBase() {

    private lateinit var tvClientFullName: TextView
    private lateinit var addGoodBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)

        val view = findViewById<View>(R.id.add_debt)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initTopBarFragment(
            getString(R.string.add_debt_activity_title),
            R.drawable.ic_add_debt_24
        )

        val selectedPurchaser: Purchaser? =
            intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)

        tvClientFullName = view.findViewById(R.id.add_debt_tv_client_full_name)
        selectedPurchaser?.let {
            tvClientFullName.text = it.fullName()
        }

        addGoodBtn = view.findViewById<Button?>(R.id.all_clients_item_btn_add_good).apply {
            setOnClickListener {
                val singleItems =
                    arrayOf("Сканировать штрихкод", "Выбрать из списка", "Ввести штрихкод вручную")

                MaterialAlertDialogBuilder(context)
                    .setTitle(resources.getString(R.string.add_good_options_text))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.proceed)) { dialog, which ->

                    }
                    // Single-choice items (initialized with checked item)
                    .setSingleChoiceItems(singleItems, 0) { dialog, checkedItem ->
                        when (checkedItem) {
                            0 -> startActivity(
                                LiveBarcodeScanningActivity::class.java,
                                selectedPurchaser
                            )
                            1 -> startActivity(
                                SelectableGoodListActivity::class.java,
                                selectedPurchaser
                            )
                        }
                    }
                    .show()
            }
        }
    }

    private fun startActivity(
        activityClass: Class<out AppCompatActivity>,
        selectedPurchaser: Purchaser?,
    ) {
        val intent = Intent(this, activityClass).apply {
            //putExtra(IntentExtras.SELECTED_PURCHASER, selectedItem)
        }
        this.startActivity(intent)
    }
}