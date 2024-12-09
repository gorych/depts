package com.gorych.debts.core.dialog

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gorych.debts.R
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder
import com.gorych.debts.utility.ToastUtils.Companion.toast

class RecyclerViewItemConfirmationRemovalDialog<T>(
    private val context: Context,
    private val adapter: BaseAdapter<out T, out BaseViewHolder<T>>,
    private val itemPosition: Int,
    private val titleResId: Int,
    private val messageResId: Int,
    private val toastResId: Int
) {
    fun show() {
        MaterialAlertDialogBuilder(context)
            .setTitle(titleResId)
            .setMessage(messageResId)
            .setNegativeButton(R.string.no) { dialog, _ ->
                adapter.notifyItemChanged(itemPosition)
                dialog.dismiss()
            }
            .setPositiveButton(R.string.yes) { dialog, _ ->
                adapter.removeItem(itemPosition)
                toast(toastResId)
                dialog.dismiss()
            }
            .show()
    }
}