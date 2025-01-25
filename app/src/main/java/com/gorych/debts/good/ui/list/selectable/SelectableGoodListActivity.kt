package com.gorych.debts.good.ui.list.selectable

import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.gorych.debts.R
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.watcher.AbstractOnTextChangedWatcher
import com.gorych.debts.good.Good
import com.gorych.debts.good.contract.SelectableGoodListContract
import com.gorych.debts.good.presenter.SelectableGoodListPresenter
import com.gorych.debts.good.repository.GoodRepository
import com.gorych.debts.core.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import kotlinx.coroutines.launch

class SelectableGoodListActivity : TopBarActivityBase(), SelectableGoodListContract.View {

    private lateinit var tvClientFullName: TextView
    private lateinit var goodCountChip: Chip

    private lateinit var itemsRecyclerView: RecyclerView

    private lateinit var goodListPresenter: SelectableGoodListContract.Presenter
    private lateinit var goodItemAdapter: BaseAdapter<Good, SelectableGoodItemAdapter.GoodItemViewHolder>

    private val goodRepository: GoodRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        GoodRepository(database.goodDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectable_good_list)

        itemsRecyclerView = findViewById(R.id.goods_selection_rv_items)

        ViewCompat.setOnApplyWindowInsetsListener(itemsRecyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        goodItemAdapter = SelectableGoodItemAdapter()
        goodListPresenter = SelectableGoodListPresenter(this, goodRepository)

        initTopBarFragment(
            R.string.goods_selection_title,
            R.drawable.ic_add_list_24
        )

        val selectedPurchaser: Purchaser? =
            intent.getParcelableExtra(IntentExtras.SELECTED_PURCHASER)

        tvClientFullName = findViewById(R.id.goods_selection_tv_client_full_name)
        selectedPurchaser?.let {
            tvClientFullName.text = it.fullName()
        }

        initSearchBar()
        initItemsView()

        goodCountChip = findViewById(R.id.goods_selection_good_count)

        lifecycleScope.launch {
            goodListPresenter.loadInitialList()
        }
    }

    private fun initItemsView() {
        itemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SelectableGoodListActivity)
            adapter = goodItemAdapter
            setHasFixedSize(true)
        }
    }

    private fun initSearchBar() {
        findViewById<TextInputEditText>(R.id.goods_selection_search_bar).addTextChangedListener(
            object : AbstractOnTextChangedWatcher() {
                override fun afterTextChanged(text: Editable?) {
                    lifecycleScope.launch {
                        goodListPresenter.reloadListOnSearch(text.toString())
                    }
                }
            })
    }

    override fun populateItems(goods: List<Good>, count: Int) {
        goodItemAdapter.updateItems(goods)
        goodCountChip.text = getString(R.string.total_goods_template_string, count)
    }
}