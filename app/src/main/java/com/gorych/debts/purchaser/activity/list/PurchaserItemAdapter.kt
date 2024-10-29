package com.gorych.debts.purchaser.activity.list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.R
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.activity.single.SingleClientInfoActivity
import com.gorych.debts.purchaser.activity.list.PurchaserItemAdapter.PurchaserItemViewHolder

class PurchaserItemAdapter(private val purchasers: List<Purchaser>, private val context: Context) :
    RecyclerView.Adapter<PurchaserItemViewHolder>() {

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

        private val clientFullNameView: TextView =
            view.findViewById(R.id.all_clients_item_tv_full_name)
        private val clientPhoneView: TextView =
            view.findViewById(R.id.all_clients_item_tv_phone)
        private val clientDebtsView: TextView =
            view.findViewById(R.id.all_clients_item_tv_debts)

        fun bind(purchaser: Purchaser) {
            clientFullNameView.text = purchaser.fullName()

            when {
                purchaser.phoneNumber != null -> {
                    clientPhoneView.text =
                        context.getString(R.string.client_phone_view_prefix, purchaser.phoneNumber)
                }

                else -> {
                    clientPhoneView.visibility = View.GONE
                }
            }

            clientDebtsView.text =
                if (purchaser.hasActiveDebts()) context.getString(R.string.client_debts_text_view_YES_value)
                else context.getString(R.string.client_debts_text_view_NO_value)

            itemView.setOnClickListener {
                val intent = Intent(context, SingleClientInfoActivity::class.java).apply {
                    putExtra(IntentExtras.SELECTED_PURCHASER, purchaser)
                }
                context.startActivity(intent)
            }
        }
    }
}