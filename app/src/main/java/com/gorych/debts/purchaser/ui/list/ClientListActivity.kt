package com.gorych.debts.purchaser.ui.list

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.gorych.debts.R
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.core.callback.RecyclerViewItemRightSwipeDeleteCallback
import com.gorych.debts.core.dialog.RecyclerViewItemConfirmationRemovalDialog
import com.gorych.debts.core.IntentExtras
import com.gorych.debts.core.watcher.AbstractOnTextChangedWatcher
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.contract.PurchaserListContract
import com.gorych.debts.purchaser.presenter.PurchaserListPresenter
import com.gorych.debts.purchaser.repository.PurchaserRepository
import com.gorych.debts.purchaser.ui.add.AddClientActivity
import kotlinx.coroutines.launch

class ClientListActivity : TopBarActivityBase(), PurchaserListContract.View {

    private lateinit var purchaserListPresenter: PurchaserListContract.Presenter
    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var purchaserAdapter: PurchaserItemAdapter

    private lateinit var purchaserCountChip: Chip

    private val purchaserRepository: PurchaserRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        PurchaserRepository(database.purchaserDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_all_clients)

        initItemsRecyclerView()

        ViewCompat.setOnApplyWindowInsetsListener(itemsRecyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        purchaserAdapter = PurchaserItemAdapter(this)
        purchaserListPresenter = PurchaserListPresenter(this, purchaserRepository)

        initTopBarFragment(
            R.string.mode_show_all_clients_title,
            R.drawable.ic_people_24
        )

        initSearchBar()

        initItemsView()

        purchaserCountChip = findViewById(R.id.client_count)

        lifecycleScope.launch {
            purchaserListPresenter.loadInitialList()
        }

        findViewById<FloatingActionButton>(R.id.all_clients_fab_add_person).setOnClickListener {
            this.startActivity(Intent(this, AddClientActivity::class.java).apply {
                putExtra(
                    IntentExtras.PREVIOUS_ACTIVITY_NAME,
                    this@ClientListActivity::class.java.name
                )
            })
        }
    }

    override fun populateItems(purchasers: List<Purchaser>, count: Int) {
        purchaserCountChip.text = getString(R.string.found_client_template_string, count)
        purchaserAdapter.updateItems(purchasers)
    }

    override fun removeItem(purchaser: Purchaser) {
        lifecycleScope.launch {
            purchaserRepository.remove(purchaser)
        }
    }

    private fun initSearchBar() {
        findViewById<TextInputEditText>(R.id.all_clients_search_bar).addTextChangedListener(
            object : AbstractOnTextChangedWatcher() {
                override fun afterTextChanged(text: Editable?) {
                    lifecycleScope.launch {
                        purchaserListPresenter.reloadListOnSearch(text.toString())
                    }
                }
            })
    }

    private fun initItemsRecyclerView() {
        itemsRecyclerView = findViewById(R.id.all_clients_rv_items)
        ItemTouchHelper(
            RecyclerViewItemRightSwipeDeleteCallback(
                this,
            ) { position -> showConfirmationRemovalDialog(position) }
        ).apply { attachToRecyclerView(itemsRecyclerView) }
    }

    private fun initItemsView() {
        itemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ClientListActivity)
            adapter = purchaserAdapter
            setHasFixedSize(true)
        }
    }

    private fun showConfirmationRemovalDialog(position: Int) {
        RecyclerViewItemConfirmationRemovalDialog(
            this,
            purchaserAdapter,
            position,
            R.string.purchaser_removal_confirmation_text,
            R.string.purchaser_removal_confirmation_question,
            R.string.purchaser_removed_text
        ).show()
    }
}