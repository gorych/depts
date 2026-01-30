package com.gorych.debts.purchaser.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorych.debts.R
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder
import com.gorych.debts.receipt.Receipt

class DebtRecyclerViewAdapter :
    BaseAdapter<Receipt, DebtRecyclerViewAdapter.DebtItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtItemViewHolder {
        return DebtItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_activity_client_info_debts, parent, false
                )
        )
    }

    class DebtItemViewHolder(itemView: View) : BaseViewHolder<Receipt>(itemView) {
        val itemViewContext: Context = itemView.context

        private val debtNameView: TextView = itemView.findViewById(R.id.client_info_tv_debt_name)
        private val debtSellerView: TextView =
            itemView.findViewById(R.id.client_info_tv_debt_seller)
        private val debtCreationDateView: TextView =
            itemView.findViewById(R.id.client_info_tv_debt_creation_date)

        override fun bind(item: Receipt) {
            debtNameView.text = itemViewContext.getString(
                R.string.receipt_number_template_string,
                item.id
            )
            debtSellerView.text = itemViewContext.getString(
                R.string.seller_template_string,
                item.seller
            )
            debtCreationDateView.text = itemViewContext.getString(
                R.string.created_at_template_string,
                item.createdAtFormatted
            )
        }
    }
}