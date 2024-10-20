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
import com.gorych.debts.purchaser.Purchaser

class ShowAllClientsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_show_all_clients)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clients_recycler_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val purchasers = purchasers()

        findViewById<RecyclerView>(R.id.clients_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShowAllClientsActivity)
            adapter = PurchaserItemAdapter(purchasers)
        }
    }

    private fun purchasers(): List<Purchaser> {
        val purchasers =
            (1..100).map {
                Purchaser(
                    "Yahor$it",
                    "Semianchenia$it",
                    "+375 25 1594702",
                    it % 2 == 0
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
                        R.layout.clients_activity_item, parent, false
                    )
            )
        }

        override fun getItemCount(): Int = purchasers.size

        override fun onBindViewHolder(holder: PurchaserItemViewHolder, position: Int) =
            holder.bind(purchasers[position])

        inner class PurchaserItemViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

            private val clientFullNameView: TextView = view.findViewById(R.id.client_full_name)
            private val clientDetailsView: TextView = view.findViewById(R.id.client_details)

            fun bind(purchaser: Purchaser) {
                clientFullNameView.text = purchaser.fullName()
                clientDetailsView.text = purchaser.details()
            }
        }
    }
}