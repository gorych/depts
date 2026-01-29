package com.gorych.debts.home

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.ApplicationMode
import com.gorych.debts.R
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.receipt.dao.ReceiptDao
import com.gorych.debts.utility.PermissionUtils.allPermissionsGranted
import com.gorych.debts.utility.PermissionUtils.requestRuntimePermissions
import com.gorych.debts.utility.PreferenceUtils
import com.gorych.debts.utility.ToastUtils.Companion.toast
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : TopBarActivityBase(), ApplicationModeContract.View {

    private lateinit var applicationModeAdapter: ApplicationModeItemAdapter
    private lateinit var applicationModePresenter: ApplicationModeContract.Presenter

    private val receiptDao: ReceiptDao by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        database.receiptDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)

        configureSellerId()

        applicationModeAdapter = ApplicationModeItemAdapter()
        applicationModePresenter = ApplicationModePresenter(this)

        initTopBarFragment(R.string.app_name, R.drawable.ic_attach_money_24)
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

    private fun configureSellerId() {
        try {
            val sellerId = PreferenceUtils.getStringPref(this, R.string.pref_key_seller_id)
            if (sellerId == null) {
                //TODO add popup to set sellerId when first start.
                val sellerId: String =
                    Settings.System.getString(contentResolver, "device_name")
                        ?: UUID.randomUUID().toString().substring(0, 8)
                PreferenceUtils.saveStringPreference(this, R.string.pref_key_seller_id, sellerId)
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Error while configuring seller ID", e)
        }
    }

    private fun initDebtsCountView() {
        findViewById<TextView>(R.id.all_clients_tv_count_of_active_debts).apply {
            lifecycleScope.launch {
                val allDebtsCount = receiptDao.countOpen()
                text = allDebtsCount.toString()
                setOnClickListener {
                    toast(getString(R.string.all_debts_count, allDebtsCount))
                }
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

    companion object {
        private val TAG = MainActivity::class.simpleName
    }
}