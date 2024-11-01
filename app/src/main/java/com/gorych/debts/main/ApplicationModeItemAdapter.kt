package com.gorych.debts.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorych.debts.R
import com.gorych.debts.adapter.BaseAdapter
import com.gorych.debts.adapter.BaseViewHolder

class ApplicationModeItemAdapter :
    BaseAdapter<ApplicationMode, ApplicationModeItemAdapter.ApplicationModeItmViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApplicationModeItmViewHolder {
        return ApplicationModeItmViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.application_mode_item, parent, false
                )
        )
    }

    inner class ApplicationModeItmViewHolder(view: View) : BaseViewHolder<ApplicationMode>(view) {

        private val titleView: TextView = view.findViewById(R.id.main_tv_mode_title)
        private val subtitleView: TextView = view.findViewById(R.id.main_tv_mode_subtitle)

        override fun bind(item: ApplicationMode) {
            subtitleView.setText(item.subtitleResId)
            titleView.setText(item.titleResId)
            itemView.setOnClickListener {
                val context = itemView.context
                val activityClass =
                    ApplicationMode.entries.first { it == item }.activityClass
                context.startActivity(Intent(context, activityClass))
            }
        }
    }
}