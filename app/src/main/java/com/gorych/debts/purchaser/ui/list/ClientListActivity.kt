package com.gorych.debts.purchaser.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gorych.debts.R
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserListContract
import com.gorych.debts.purchaser.presenter.PurchaserListPresenter
import com.gorych.debts.purchaser.repository.PurchaserRepository
import com.gorych.debts.purchaser.ui.add.AddClientActivity

class ClientListActivity : TopBarActivityBase(), PurchaserListContract.View {

    private lateinit var purchaserListPresenter: PurchaserListContract.Presenter
    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var purchaserAdapter: PurchaserItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_all_clients)

        itemsRecyclerView = findViewById(R.id.all_clients_rv_items)

        ViewCompat.setOnApplyWindowInsetsListener(itemsRecyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        purchaserAdapter = PurchaserItemAdapter()
        val purchaserDao = AppDatabase.getDatabase(this).purchaserDao()
        purchaserListPresenter = PurchaserListPresenter(this, PurchaserRepository(purchaserDao))

        initTopBarFragment(
            R.string.all_clients_activity_title,
            R.drawable.ic_people_24
        )
        initItemsView()

        purchaserListPresenter.loadInitialList()

        findViewById<FloatingActionButton>(R.id.all_clients_fab_add_person).setOnClickListener {
            this.startActivity(Intent(this, AddClientActivity::class.java))
        }
    }

    override fun populateItems(purchasers: List<Purchaser>) {
        purchaserAdapter.updateItems(purchasers)
    }

    private fun initItemsView() {
        itemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ClientListActivity)
            adapter = purchaserAdapter
            setHasFixedSize(true)
        }
    }
}