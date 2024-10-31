package com.gorych.debts.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : BaseViewHolder<T>>(private var items: List<T> = listOf()) :
    RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateCustomViewHolder(parent, viewType)
    }

    abstract fun onCreateCustomViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<T>) {
        items = newItems
        notifyDataSetChanged()
    }
}