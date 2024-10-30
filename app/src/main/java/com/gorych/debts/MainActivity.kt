package com.gorych.debts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.debt.repository.DebtRepository
import com.gorych.debts.utility.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var debtRepository: DebtRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)

        debtRepository = DebtRepository()
        findViewById<TextView>(R.id.all_clients_tv_count_of_active_debts).apply {
            val allDebtsCount = debtRepository.getAllCount()
            text = allDebtsCount.toString()
            setOnClickListener {
                showCountOfDebtsToast(allDebtsCount)
            }
        }

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

    private fun showCountOfDebtsToast(count: Int) {
        Toast
            .makeText(
                this@MainActivity,
                getString(R.string.debts_count, count), Toast.LENGTH_SHORT
            )
            .show()
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