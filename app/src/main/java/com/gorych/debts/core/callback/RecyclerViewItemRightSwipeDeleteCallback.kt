package com.gorych.debts.core.callback

import android.content.Context
import com.gorych.debts.R

class RecyclerViewItemRightSwipeDeleteCallback(
    context: Context,
    swipeAction: (position: Int) -> Unit
) : RecyclerViewItemRightSwipeCallback(
    context,
    R.drawable.ic_delete_forever_24,
    R.color.red_lighten_1,
    swipeAction
)

