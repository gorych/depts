package com.gorych.debts.core.callback

import androidx.recyclerview.widget.ItemTouchHelper
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder

class RecyclerViewItemRightSwipeCallback<T>(adapter: BaseAdapter<out T, out BaseViewHolder<T>>) :
    RecyclerViewItemSwipeCallback<T>(
        adapter,
        ItemTouchHelper.RIGHT
    )