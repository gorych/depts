package com.gorych.debts.purchaser.ui.detail

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.gorych.debts.R
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.IntentExtras
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.receipt.repository.ReceiptRepository
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserDetailContract
import com.gorych.debts.purchaser.presenter.PurchaserDetailPresenter
import com.gorych.debts.receipt.Receipt
import com.gorych.debts.utility.ClipboardUtils.copyTextToClipboard
import com.gorych.debts.utility.ToastUtils.Companion.toast
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.textAsString
import kotlinx.coroutines.launch

class ClientDetailActivity : TopBarActivityBase(), PurchaserDetailContract.View {

    private lateinit var topBarTitle: String
    private lateinit var tvPhone: TextView
    private lateinit var rvDebts: RecyclerView

    private lateinit var debtItemAdapter: DebtRecyclerViewAdapter

    private lateinit var purchaserDetailPresenter: PurchaserDetailContract.Presenter

    private val receiptRepository: ReceiptRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        ReceiptRepository(database.receiptDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_client_info)

        tvPhone = findViewById(R.id.client_info_tv_phone)
        rvDebts = findViewById(R.id.client_details_rv_debts)

        ViewCompat.setOnApplyWindowInsetsListener(rvDebts) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        debtItemAdapter = DebtRecyclerViewAdapter()
        purchaserDetailPresenter = PurchaserDetailPresenter(this, receiptRepository)

        val selectedPurchaser: Purchaser? =
            intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)
        selectedPurchaser?.let {
            populatePersonalInfo(it)

            initTopBarFragment(topBarTitle, R.drawable.ic_person_24)
            initPhoneView(it)
            initActiveDebtsOnlyCheckBox(it)
            initDebtsView()

            lifecycleScope.launch {
                purchaserDetailPresenter.loadActiveDebts(it)
            }
        }
    }

    override fun populateDebts(receipts: List<Receipt>) {
        debtItemAdapter.updateItems(receipts)
    }

    override fun populatePersonalInfo(purchaser: Purchaser) {
        topBarTitle = purchaser.fullName()
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
                lifecycleScope.launch {
                    purchaserDetailPresenter.reloadDebts(
                        purchaser = purchaser,
                        activeDebtsOnly = isActiveDebtsOnlyChecked
                    )
                }
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
                    toast(getString(R.string.copied))
                }
            }

            else -> {
                findViewById<ConstraintLayout>(R.id.client_details_c_layout_phone).hide()
            }
        }
    }

    companion object {
        const val LABEL_PHONE_NUMBER = "phoneNumberLabel"
    }
}