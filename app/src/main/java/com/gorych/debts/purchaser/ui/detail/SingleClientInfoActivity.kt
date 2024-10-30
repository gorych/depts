package com.gorych.debts.purchaser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.gorych.debts.debt.repository.PurchaserDebtRepository
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserDetailContract
import com.gorych.debts.utility.ClipboardUtils.copyTextToClipboard
import com.gorych.debts.utility.hide

class SingleClientInfoActivity : AppCompatActivity(), PurchaserDetailContract.View {

    private lateinit var purchaserDetailPresenter: PurchaserDetailContract.Presenter
    private lateinit var purchaserDebtRepository: PurchaserDebtRepository
    private lateinit var debtsRecyclerView: RecyclerView
    private lateinit var debtItemAdapter: DebtItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_client_info)

        purchaserDebtRepository = PurchaserDebtRepository()
        debtsRecyclerView = findViewById(R.id.client_details_rv_debts)

        ViewCompat.setOnApplyWindowInsetsListener(debtsRecyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val selectedPurchaser: Purchaser? =
            intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)

        selectedPurchaser?.let {
            //purchaserDetailPresenter.loadActiveDebts(it)

            val activeDebts = purchaserDebtRepository.getActiveDebtsOfPurchaser(it)
            debtItemAdapter = DebtItemAdapter(activeDebts)

            findViewById<TextView>(R.id.client_details_tv_title).text = it.fullName()

            bindPhoneView(it)
            bindActiveDebtsOnlyCheckBox(it)
            bindDebtsView()
        }
    }

    private fun bindDebtsView() {
        debtsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SingleClientInfoActivity)
            adapter = debtItemAdapter
        }
    }

    private fun bindActiveDebtsOnlyCheckBox(purchaser: Purchaser) {
        findViewById<MaterialCheckBox>(R.id.client_info_mcb_active_debts_only)
            .setOnCheckedChangeListener { _, isChecked ->
                val newItems: List<Debt> =
                    if (isChecked) purchaserDebtRepository.getActiveDebtsOfPurchaser(purchaser)
                    else purchaserDebtRepository.getAllDebtsOfPurchaser(purchaser)
                debtItemAdapter.updateItems(newItems)
            }
    }

    private fun bindPhoneView(purchaser: Purchaser) {
        when {
            purchaser.phoneNumber != null -> {
                val phoneView: TextView = findViewById(R.id.client_info_tv_phone)
                phoneView.text = purchaser.phoneNumber

                phoneView.setOnClickListener {
                    copyTextToClipboard(
                        this@SingleClientInfoActivity,
                        LABEL_PHONE_NUMBER,
                        phoneView.text.toString()
                    )
                    shortToast(getString(R.string.copied))
                }
            }

            else -> {
                val phoneLayout =
                    findViewById<ConstraintLayout>(R.id.client_details_c_layout_phone)
                phoneLayout.hide()
            }
        }
    }

    private fun shortToast(text: String) {
        Toast
            .makeText(
                this@SingleClientInfoActivity,
                text, Toast.LENGTH_SHORT
            )
            .show()
    }


    private inner class DebtItemAdapter(private var debts: List<Debt>) :
        RecyclerView.Adapter<DebtItemAdapter.DebtItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtItemViewHolder {
            return DebtItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.client_detailed_info_activity_item, parent, false
                    )
            )
        }

        override fun getItemCount(): Int = debts.size

        override fun onBindViewHolder(holder: DebtItemViewHolder, position: Int) =
            holder.bind(debts[position])

        fun updateItems(newItems: List<Debt>) {
            debts = newItems
            notifyDataSetChanged()
        }

        inner class DebtItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val debtNameView: TextView = view.findViewById(R.id.client_info_tv_debt_name)
            private val debtCreationDateView: TextView =
                view.findViewById(R.id.client_info_tv_debt_creation_date)
            private val debtSellerView: TextView =
                view.findViewById(R.id.client_info_tv_debt_seller)

            fun bind(debt: Debt) {
                debtNameView.text = debt.name
                debtCreationDateView.text = debt.created.toString()
                debtSellerView.text = debt.seller
            }
        }
    }

    companion object {
        const val LABEL_PHONE_NUMBER = "phoneNumberLabel"
    }

    override fun populateDebts(debts: List<Debt>) {
        debtItemAdapter = DebtItemAdapter(debts)
    }
}