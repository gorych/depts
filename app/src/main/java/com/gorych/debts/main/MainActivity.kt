package com.gorych.debts.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.R
import com.gorych.debts.TopBarFragment
import com.gorych.debts.debt.repository.DebtRepository
import com.gorych.debts.purchaser.contract.ApplicationModeContract
import com.gorych.debts.utility.PermissionUtils.allPermissionsGranted
import com.gorych.debts.utility.PermissionUtils.requestRuntimePermissions
import com.gorych.debts.utility.ToastUtils.Companion.toast

class MainActivity : AppCompatActivity(), ApplicationModeContract.View {

    private lateinit var debtRepository: DebtRepository
    private lateinit var applicationModeAdapter: ApplicationModeItemAdapter
    private lateinit var applicationModePresenter: ApplicationModeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)

        initTopBarFragment()

        debtRepository = DebtRepository()
        applicationModeAdapter = ApplicationModeItemAdapter()
        applicationModePresenter = ApplicationModePresenter(this)

        initDebtsCountView()
        initModeRecyclerView()

        applicationModePresenter.loadModes()
    }

    override fun onResume() {
        super.onResume()
        if (!allPermissionsGranted(this)) {
            requestRuntimePermissions(this)
        }
    }

    override fun populateItems(modes: List<ApplicationMode>) {
        applicationModeAdapter.updateItems(modes)
    }

    private fun initTopBarFragment() {
        val fragment = TopBarFragment.newInstance(
            getString(R.string.app_name),
            R.drawable.baseline_attach_money_24
        )
        supportFragmentManager.beginTransaction()
            .replace(R.id.top_bar_fragment_container, fragment)
            .commit()
    }

    private fun initDebtsCountView() {
        findViewById<TextView>(R.id.all_clients_tv_count_of_active_debts).apply {
            val allDebtsCount = debtRepository.getAllCount()
            text = allDebtsCount.toString()
            setOnClickListener {
                toast(getString(R.string.debts_count, allDebtsCount))
            }
        }
    }

    private fun initModeRecyclerView() {
        findViewById<RecyclerView>(R.id.main_rv_items).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = applicationModeAdapter
        }
    }
}