package com.gorych.debts.purchaser.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.R
import com.gorych.debts.purchaser.Debt
import com.gorych.debts.purchaser.Good
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.Status
import java.time.LocalDate.now

class ClientDetailedInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_client_detailed_info)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clients_debts_recycler_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val purchaser: Purchaser? = intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)

        purchaser?.let {
            findViewById<TextView>(R.id.client_details_view_title).text = it.fullName()
            findViewById<TextView>(R.id.client_phone_text_view_value).text =
                getString(R.string.client_phone_view_prefix, it.phoneNumber)

            val debts = getPurchaserDebts(purchaser)

            findViewById<RecyclerView>(R.id.clients_debts_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@ClientDetailedInfoActivity)
                adapter = DebtItemAdapter(debts)
            }
        }
    }

    private fun getPurchaserDebts(purchaser: Purchaser): List<Debt> {
        val goods = listOf(
            Good(1, "Колбаса", "123456789", now()),
            Good(2, null.toString(), "987525543", now())
        )

        val debts = listOf(
            Debt(1, "Чек №1", now(), Status.OPEN, purchaser, goods, "Алла"),
            Debt(2, "Чек №2", now(), Status.CLOSED, purchaser, goods, "Наташа"),
        )
        return debts
    }

    private inner class DebtItemAdapter(private val debts: List<Debt>) :
        RecyclerView.Adapter<DebtItemAdapter.DebtItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtItemViewHolder {
            return DebtItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.clients_detailed_info_activity_item, parent, false
                    )
            )
        }

        override fun getItemCount(): Int = debts.size

        override fun onBindViewHolder(holder: DebtItemViewHolder, position: Int) =
            holder.bind(debts[position])

        inner class DebtItemViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

            private val debtNameView: TextView = view.findViewById(R.id.debt_name)
            private val debtBarcodeView: TextView = view.findViewById(R.id.debt_barcode)
            private val debtCreationDateView: TextView = view.findViewById(R.id.debt_creation_date)
            private val debtSellerView: TextView = view.findViewById(R.id.debt_seller)

            fun bind(debt: Debt) {
                debtNameView.text = debt.name
                //debtBarcodeView.text = debt.barcode
                debtCreationDateView.text = debt.created.toString()
                debtSellerView.text = debt.seller
            }
        }
    }
}