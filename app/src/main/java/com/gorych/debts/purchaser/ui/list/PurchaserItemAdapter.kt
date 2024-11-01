package com.gorych.debts.purchaser.ui.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorych.debts.R
import com.gorych.debts.adapter.BaseAdapter
import com.gorych.debts.adapter.BaseViewHolder
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.ui.detail.ClientDetailActivity

class PurchaserItemAdapter :
    BaseAdapter<Purchaser, PurchaserItemAdapter.PurchaserItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaserItemViewHolder {
        return PurchaserItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.all_clients_activity_item, parent, false
                )
        )
    }

    inner class PurchaserItemViewHolder(view: View) : BaseViewHolder<Purchaser>(view) {

        private val clientPhoneView: TextView = view.findViewById(R.id.all_clients_item_tv_phone)
        private val clientDebtsView: TextView = view.findViewById(R.id.all_clients_item_tv_debts)
        private val clientFullNameView: TextView =
            view.findViewById(R.id.all_clients_item_tv_full_name)

        override fun bind(item: Purchaser) {
            val activityContext = itemView.context
            clientFullNameView.text = item.fullName()
            when {
                item.phoneNumber != null -> {
                    clientPhoneView.text =
                        activityContext.getString(
                            R.string.client_phone_view_prefix,
                            item.phoneNumber
                        )
                }

                else -> {
                    clientPhoneView.visibility = View.GONE
                }
            }

            clientDebtsView.text =
                if (item.hasActiveDebts()) activityContext.getString(R.string.client_debts_text_view_YES_value)
                else activityContext.getString(R.string.client_debts_text_view_NO_value)

            itemView.setOnClickListener {
                val intent = Intent(activityContext, ClientDetailActivity::class.java).apply {
                    putExtra(IntentExtras.SELECTED_PURCHASER, item)
                }
                activityContext.startActivity(intent)
            }
        }
    }
}