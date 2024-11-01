package com.gorych.debts.purchaser.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorych.debts.R
import com.gorych.debts.adapter.BaseAdapter
import com.gorych.debts.adapter.BaseViewHolder
import com.gorych.debts.debt.Debt

class DebtRecyclerViewAdapter :
    BaseAdapter<Debt, DebtRecyclerViewAdapter.DebtItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtItemViewHolder {
        return DebtItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_activity_client_info_debts, parent, false
                )
        )
    }

    inner class DebtItemViewHolder(itemView: View) : BaseViewHolder<Debt>(itemView) {
        private val debtNameView: TextView = itemView.findViewById(R.id.client_info_tv_debt_name)
        private val debtSellerView: TextView = itemView.findViewById(R.id.client_info_tv_debt_seller)
        private val debtCreationDateView: TextView =
            itemView.findViewById(R.id.client_info_tv_debt_creation_date)

        override fun bind(item: Debt) {
            debtNameView.text = item.name
            debtSellerView.text = item.seller
            debtCreationDateView.text = item.created.toString()
        }
    }
}