package com.gorych.debts.purchaser.activity

import android.content.Intent
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
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser

class AllClientsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_all_clients)


        val itemsView = findViewById<RecyclerView>(R.id.all_clients_rv_items)

        ViewCompat.setOnApplyWindowInsetsListener(itemsView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        itemsView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@AllClientsActivity)
            adapter = PurchaserItemAdapter(loadPurchasers())
        }
    }

    private fun loadPurchasers(): List<Purchaser> {
        val purchasers =
            (1..100).map {
                Purchaser(
                    "$it".toLong(),
                    "Егор$it",
                    "Семенченя$it",
                    "+375 25 1594702"
                )
            }
        return purchasers
    }

    private inner class PurchaserItemAdapter(private val purchasers: List<Purchaser>) :
        RecyclerView.Adapter<PurchaserItemAdapter.PurchaserItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaserItemViewHolder {
            return PurchaserItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.all_clients_activity_item, parent, false
                    )
            )
        }

        override fun getItemCount(): Int = purchasers.size

        override fun onBindViewHolder(holder: PurchaserItemViewHolder, position: Int) =
            holder.bind(purchasers[position])

        inner class PurchaserItemViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

            private val clientFullNameView: TextView = view.findViewById(R.id.all_clients_item_tv_full_name)
            private val clientPhoneView: TextView = view.findViewById(R.id.all_clients_item_tv_phone)
            private val clientDebtsView: TextView = view.findViewById(R.id.all_clients_item_tv_debts)

            fun bind(purchaser: Purchaser) {
                clientFullNameView.text = purchaser.fullName()
                clientPhoneView.text =
                    getString(R.string.client_phone_view_prefix, purchaser.phoneNumber)
                clientDebtsView.text =
                    if (purchaser.hasActiveDebts()) getString(R.string.client_debts_text_view_YES_value)
                    else getString(R.string.client_debts_text_view_NO_value)

                itemView.setOnClickListener {
                    val activity = this@AllClientsActivity
                    val intent = Intent(activity, ClientDetailedInfoActivity::class.java).apply {
                        putExtra(IntentExtras.SELECTED_PURCHASER, purchaser)
                    }
                    activity.startActivity(intent)
                }
            }
        }
    }
}