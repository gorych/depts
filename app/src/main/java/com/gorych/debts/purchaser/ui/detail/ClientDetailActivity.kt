package com.gorych.debts.purchaser.ui.detail

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.gorych.debts.R
import com.gorych.debts.debt.Debt
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserDetailContract
import com.gorych.debts.purchaser.presenter.PurchaserDetailPresenter
import com.gorych.debts.utility.ClipboardUtils.copyTextToClipboard
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.textAsString

class ClientDetailActivity : AppCompatActivity(), PurchaserDetailContract.View {

    private lateinit var tvTitle: TextView
    private lateinit var tvPhone: TextView
    private lateinit var rvDebts: RecyclerView

    private lateinit var debtItemAdapter: DebtRecyclerViewAdapter

    private lateinit var purchaserDetailPresenter: PurchaserDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_client_info)

        tvPhone = findViewById(R.id.client_info_tv_phone)
        tvTitle = findViewById(R.id.client_details_tv_title)
        rvDebts = findViewById(R.id.client_details_rv_debts)

        ViewCompat.setOnApplyWindowInsetsListener(rvDebts) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        debtItemAdapter = DebtRecyclerViewAdapter()
        purchaserDetailPresenter = PurchaserDetailPresenter(this)

        val selectedPurchaser: Purchaser? =
            intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)
        selectedPurchaser?.let {
            populatePersonalInfo(it)

            initPhoneView(it)
            initActiveDebtsOnlyCheckBox(it)
            initDebtsView()

            purchaserDetailPresenter.loadActiveDebts(it)
        }
    }

    override fun populateDebts(debts: List<Debt>) {
        debtItemAdapter.updateItems(debts)
    }

    override fun populatePersonalInfo(purchaser: Purchaser) {
        tvTitle.text = purchaser.fullName()
        tvPhone.text = purchaser.phoneNumber
    }

    private fun initDebtsView() {
        rvDebts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ClientDetailActivity)
            adapter = debtItemAdapter
        }
    }

    private fun initActiveDebtsOnlyCheckBox(purchaser: Purchaser) {
        findViewById<MaterialCheckBox>(R.id.client_info_mcb_active_debts_only)
            .setOnCheckedChangeListener { _, isActiveDebtsOnlyChecked ->
                purchaserDetailPresenter.reloadDebts(purchaser, isActiveDebtsOnlyChecked)
            }
    }

    private fun initPhoneView(purchaser: Purchaser) {
        when {
            purchaser.phoneNumber != null -> {
                tvPhone.setOnClickListener {
                    copyTextToClipboard(
                        applicationContext,
                        LABEL_PHONE_NUMBER,
                        tvPhone.textAsString()
                    )
                    shortToast(getString(R.string.copied))
                }
            }

            else -> {
                findViewById<ConstraintLayout>(R.id.client_details_c_layout_phone).hide()
            }
        }
    }

    private fun shortToast(text: String) {
        Toast
            .makeText(
                applicationContext,
                text, Toast.LENGTH_SHORT
            )
            .show()
    }

    companion object {
        const val LABEL_PHONE_NUMBER = "phoneNumberLabel"
    }
}