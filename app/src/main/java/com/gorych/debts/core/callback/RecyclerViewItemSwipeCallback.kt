package com.gorych.debts.core.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

private const val NOT_SUPPORTED_DRAG_AND_DROP = 0

abstract class RecyclerViewItemSwipeCallback(
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(NOT_SUPPORTED_DRAG_AND_DROP, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false // Not handling movement
    }
}