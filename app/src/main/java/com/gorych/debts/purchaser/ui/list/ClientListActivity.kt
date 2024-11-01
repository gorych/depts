package com.gorych.debts.purchaser.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.R
import com.gorych.debts.TopBarActivity
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserListContract
import com.gorych.debts.purchaser.presenter.PurchaserListPresenter

class ClientListActivity : TopBarActivity(), PurchaserListContract.View {

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
        purchaserListPresenter = PurchaserListPresenter(this)

        initTopBarFragment(
            R.string.all_clients_activity_title,
            R.drawable.ic_baseline_people_24
        )
        initItemsView()

        purchaserListPresenter.loadInitialList()
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