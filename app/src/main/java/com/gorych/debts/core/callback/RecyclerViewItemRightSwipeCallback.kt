package com.gorych.debts.core.callback

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder

class RecyclerViewItemRightSwipeCallback<T>(
    adapter: BaseAdapter<out T, out BaseViewHolder<T>>,
    private val context: Context,
    private val iconResId: Int,
) : RecyclerViewItemSwipeCallback<T>(
    adapter,
    ItemTouchHelper.RIGHT
) {
    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val icon = ContextCompat.getDrawable(context, iconResId)
        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height / 2) - (icon.intrinsicHeight / 2)
        val iconBottom = iconTop + icon.intrinsicHeight

        when {
            dX > 0 -> {
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
            else -> {
                icon.setBounds(0, 0, 0, 0)
            }
        }

        icon.draw(canvas)
    }
}

