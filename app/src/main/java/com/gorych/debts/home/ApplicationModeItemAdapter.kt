package com.gorych.debts.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gorych.debts.ApplicationMode
import com.gorych.debts.R
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.adapter.BaseViewHolder

class ApplicationModeItemAdapter :
    BaseAdapter<ApplicationMode, ApplicationModeItemAdapter.ApplicationModeItmViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApplicationModeItmViewHolder {
        return ApplicationModeItmViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_main_activity_modes, parent, false
                )
        )
    }

    inner class ApplicationModeItmViewHolder(view: View) : BaseViewHolder<ApplicationMode>(view) {

        private val titleView: TextView = view.findViewById(R.id.main_tv_mode_title)
        private val subtitleView: TextView = view.findViewById(R.id.main_tv_mode_subtitle)
        private val iconView: ImageView = view.findViewById(R.id.main_iv_item_icon)

        override fun bind(item: ApplicationMode) {
            val mode = ApplicationMode.entries.first { it == item }

            subtitleView.setText(item.subtitleResId)
            titleView.setText(item.titleResId)
            iconView.apply {
                setImageResource(mode.iconResId)
                contentDescription = titleView.text
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val activityClass =
                    mode.activityClass
                context.startActivity(Intent(context, activityClass))
            }
        }
    }
}