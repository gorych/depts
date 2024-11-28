package com.gorych.debts.good.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorych.debts.R
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder
import com.gorych.debts.good.Good
import com.gorych.debts.good.contract.GoodListContract
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.show

class GoodItemAdapter(
    private val itemClickAction: (selectedGood: Good) -> Unit,
    private val view: GoodListContract.View
) : BaseAdapter<Good, GoodItemAdapter.GoodItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodItemViewHolder {
        return GoodItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_activity_all_goods, parent, false
                )
        )
    }

    override fun removeItem(position: Int) {
        val selectedGood = items[position]
        view.removeItem(selectedGood)
        super.removeItem(position)
    }

    inner class GoodItemViewHolder(itemView: View) : BaseViewHolder<Good>(itemView) {
        private val barcodeView: TextView =
            itemView.findViewById(R.id.all_goods_item_tv_barcode)
        private val createdAtView: TextView =
            itemView.findViewById(R.id.all_goods_item_tv_createdAt)
        private val nameView: TextView =
            itemView.findViewById(R.id.all_goods_item_tv_name)

        override fun bind(item: Good) {
            itemView.setOnClickListener {
                itemClickAction(item)
            }
            barcodeView.text = item.barcode
            createdAtView.text = item.createdAtFormatted
            item.name?.let {
                nameView.text = item.name
                nameView.show()
            } ?: run {
                nameView.hide()
            }
        }
    }

}
