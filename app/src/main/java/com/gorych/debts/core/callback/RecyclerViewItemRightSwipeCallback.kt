package com.gorych.debts.core.callback

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.R

open class RecyclerViewItemRightSwipeCallback(
    private val context: Context,
    private val iconResId: Int,
    private val backgroundResId: Int?,
    private val swipeAction: (position: Int) -> Unit
) : RecyclerViewItemSwipeCallback(ItemTouchHelper.RIGHT) {

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        swipeAction(position)
    }

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
        backgroundResId?.let {
            changeBackgroundColor(itemView, it)
        }

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

    private fun changeBackgroundColor(itemView: View, colorResId: Int): Boolean {
        itemView.setBackgroundResource(colorResId)
        return itemView.postDelayed({
            itemView.setBackgroundResource(R.color.black)
        }, 1000)
    }
}

