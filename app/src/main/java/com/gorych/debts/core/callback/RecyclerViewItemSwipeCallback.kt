package com.gorych.debts.core.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder

private const val NOT_SUPPORTED_DRAG_AND_DROP = 0

class RecyclerViewItemSwipeCallback<T>(
    val adapter: BaseAdapter<T, BaseViewHolder<T>>,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(NOT_SUPPORTED_DRAG_AND_DROP, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false // Not handling movement
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.removeItem(position)
    }
}