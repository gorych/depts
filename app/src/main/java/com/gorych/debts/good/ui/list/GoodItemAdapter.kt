package com.gorych.debts.good.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorych.debts.R
import com.gorych.debts.adapter.BaseAdapter
import com.gorych.debts.adapter.BaseViewHolder
import com.gorych.debts.good.Good

class GoodItemAdapter : BaseAdapter<Good, GoodItemAdapter.GoodItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodItemViewHolder {
        return GoodItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_activity_all_goods, parent, false
                )
        )
    }

    inner class GoodItemViewHolder(itemView: View) : BaseViewHolder<Good>(itemView) {

        private val barcodeView: TextView =
            itemView.findViewById(R.id.all_goods_item_tv_barcode)
        private val nameView: TextView =
            itemView.findViewById(R.id.all_goods_item_tv_name)
        private val createdAtView: TextView =
            itemView.findViewById(R.id.all_goods_item_tv_createdAt)

        override fun bind(item: Good) {
            barcodeView.text = item.barcode
            nameView.text = item.name ?: ""
            createdAtView.text = item.createdAt.toString()
        }
    }

}
