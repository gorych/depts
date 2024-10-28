package com.gorych.debts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.util.Utils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.main_rv_items).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ModeItemAdapter(ApplicationMode.entries.toTypedArray())
        }
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }

    private inner class ModeItemAdapter(private val applicationModes: Array<ApplicationMode>) :
        RecyclerView.Adapter<ModeItemAdapter.ModeItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeItemViewHolder {
            return ModeItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.application_mode_item, parent, false
                    )
            )
        }

        override fun onBindViewHolder(modeItemViewHolder: ModeItemViewHolder, position: Int) =
            modeItemViewHolder.bindDetectionMode(applicationModes[position])

        override fun getItemCount(): Int = applicationModes.size

        private inner class ModeItemViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

            private val titleView: TextView = view.findViewById(R.id.main_tv_mode_title)
            private val subtitleView: TextView = view.findViewById(R.id.main_tv_mode_subtitle)

            fun bindDetectionMode(applicationMode: ApplicationMode) {
                titleView.setText(applicationMode.titleResId)
                subtitleView.setText(applicationMode.subtitleResId)
                itemView.setOnClickListener {
                    val activity = this@MainActivity
                    val activityClass =
                        ApplicationMode.entries.first { it == applicationMode }.activityClass
                    activity.startActivity(Intent(this@MainActivity, activityClass))
                }
            }
        }
    }
}