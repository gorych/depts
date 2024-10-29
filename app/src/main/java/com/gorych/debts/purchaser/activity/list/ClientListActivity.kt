package com.gorych.debts.purchaser.activity.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.R
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.presenter.PurchaserListPresenter
import com.gorych.debts.purchaser.view.PurchaserListView

class ClientListActivity : AppCompatActivity(), PurchaserListView {

    private lateinit var purchaserListPresenter: PurchaserListPresenter
    private lateinit var itemsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_all_clients)

        itemsView = findViewById(R.id.all_clients_rv_items)

        ViewCompat.setOnApplyWindowInsetsListener(itemsView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        purchaserListPresenter = PurchaserListPresenter(this)

        purchaserListPresenter.loadItems()
    }

    override fun populateItems(purchasers: List<Purchaser>) {
        itemsView.apply {
            val activity = this@ClientListActivity
            layoutManager = LinearLayoutManager(activity)
            adapter = PurchaserItemAdapter(purchasers, activity)
            setHasFixedSize(true)
        }
    }
}