package com.gorych.debts.purchaser.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
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

class ClientInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_client_info)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.client_details_rv_debts)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val purchaser: Purchaser? = intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)

        purchaser?.let {
            findViewById<TextView>(R.id.client_details_tv_title).text = it.fullName()

            bindPhoneView(it)

            findViewById<RecyclerView>(R.id.client_details_rv_debts).apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@ClientInfoActivity)
                adapter = DebtItemAdapter(getPurchaserDebts(purchaser))
            }
        }
    }

    private fun bindPhoneView(purchaser: Purchaser) {
        when {
            purchaser.phoneNumber != null -> {
                val phoneView = findViewById<TextView>(R.id.client_details_tv_phone)
                phoneView.text = purchaser.phoneNumber
            }

            else -> {
                val phoneLayout =
                    findViewById<ConstraintLayout>(R.id.client_details_c_layout_phone)
                phoneLayout.visibility = View.GONE
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
                        R.layout.client_detailed_info_activity_item, parent, false
                    )
            )
        }

        override fun getItemCount(): Int = debts.size

        override fun onBindViewHolder(holder: DebtItemViewHolder, position: Int) =
            holder.bind(debts[position])

        inner class DebtItemViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

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
}