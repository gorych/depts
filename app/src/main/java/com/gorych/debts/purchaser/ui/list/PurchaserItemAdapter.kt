package com.gorych.debts.purchaser.ui.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorych.debts.R
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.ui.detail.ClientDetailActivity
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.show

class PurchaserItemAdapter :
    BaseAdapter<Purchaser, PurchaserItemAdapter.PurchaserItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaserItemViewHolder {
        return PurchaserItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_activity_all_clients, parent, false
                )
        )
    }

    inner class PurchaserItemViewHolder(itemVIew: View) : BaseViewHolder<Purchaser>(itemVIew) {

        private val clientPhoneView: TextView =
            itemView.findViewById(R.id.all_clients_item_tv_phone)
        private val clientDebtsView: TextView =
            itemView.findViewById(R.id.all_clients_item_tv_debts)
        private val clientFullNameView: TextView =
            itemView.findViewById(R.id.all_clients_item_tv_full_name)

        override fun bind(item: Purchaser) {
            val itemViewContext = itemView.context
            clientFullNameView.text = item.fullName()
            when {
                item.phoneNumber != null -> {
                    clientPhoneView.text =
                        itemViewContext.getString(
                            R.string.client_phone_view_prefix,
                            item.phoneNumber
                        )
                    clientPhoneView.show()
                }

                else -> {
                    clientPhoneView.hide()
                }
            }

            clientDebtsView.text =
                if (item.hasActiveDebts()) itemViewContext.getString(R.string.client_debts_text_view_YES_value)
                else itemViewContext.getString(R.string.client_debts_text_view_NO_value)

            itemView.setOnClickListener {
                val intent = Intent(itemViewContext, ClientDetailActivity::class.java).apply {
                    putExtra(IntentExtras.SELECTED_PURCHASER, item)
                }
                itemViewContext.startActivity(intent)
            }
        }
    }
}