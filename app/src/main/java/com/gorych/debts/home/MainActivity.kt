package com.gorych.debts.home

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.ApplicationMode
import com.gorych.debts.R
import com.gorych.debts.TopBarActivityBase
import com.gorych.debts.debt.repository.DebtRepository
import com.gorych.debts.utility.PermissionUtils.allPermissionsGranted
import com.gorych.debts.utility.PermissionUtils.requestRuntimePermissions
import com.gorych.debts.utility.ToastUtils.Companion.toast

class MainActivity : TopBarActivityBase(), ApplicationModeContract.View {

    private lateinit var debtRepository: DebtRepository
    private lateinit var applicationModeAdapter: ApplicationModeItemAdapter
    private lateinit var applicationModePresenter: ApplicationModeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)

        debtRepository = DebtRepository()
        applicationModeAdapter = ApplicationModeItemAdapter()
        applicationModePresenter = ApplicationModePresenter(this)

        initTopBarFragment(R.string.app_name, R.drawable.ic_baseline_attach_money_24)
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