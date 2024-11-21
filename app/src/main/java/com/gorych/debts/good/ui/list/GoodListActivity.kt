package com.gorych.debts.good.ui.list

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorych.debts.R
import com.gorych.debts.TopBarActivityBase
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.good.Good
import com.gorych.debts.good.contract.GoodListContract
import com.gorych.debts.good.presenter.GoodListPresenter
import com.gorych.debts.good.repository.GoodRepository
import com.gorych.debts.purchaser.ui.add.AddClientActivity
import kotlinx.coroutines.launch

class GoodListActivity : TopBarActivityBase(), GoodListContract.View {

    private lateinit var goodListPresenter: GoodListContract.Presenter
    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var goodItemAdapter: GoodItemAdapter

    private val goodRepository: GoodRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        GoodRepository(database.goodDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_all_goods)

        itemsRecyclerView = findViewById(R.id.all_goods_rv_items)

        ViewCompat.setOnApplyWindowInsetsListener(itemsRecyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        goodItemAdapter = GoodItemAdapter { onItemCLick() }
        goodListPresenter = GoodListPresenter(this, goodRepository)

        initTopBarFragment(
            R.string.mode_show_all_goods_title,
            R.drawable.ic_baseline_goods_24
        )
        initItemsView()

        lifecycleScope.launch {
            goodListPresenter.loadInitialList()
        }
    }

    private fun onItemCLick() {
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        //TODO implement item transition
        val intent = Intent(this, AddClientActivity::class.java)
        startActivity(intent, options.toBundle())
    }

    override fun populateItems(goods: List<Good>) {
        goodItemAdapter.updateItems(goods)
    }

    private fun initItemsView() {
        itemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@GoodListActivity)
            adapter = goodItemAdapter
            setHasFixedSize(true)
        }
    }
}