package com.gorych.debts.good.ui.list

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gorych.debts.R
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.core.adapter.BaseAdapter
import com.gorych.debts.core.callback.RecyclerViewItemRightSwipeCallback
import com.gorych.debts.good.Good
import com.gorych.debts.good.contract.GoodListContract
import com.gorych.debts.good.presenter.GoodListPresenter
import com.gorych.debts.good.repository.GoodRepository
import com.gorych.debts.utility.BitmapUtils.createBitmapFromGood
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.show
import kotlinx.coroutines.launch

class GoodListActivity : TopBarActivityBase(), GoodListContract.View {

    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var goodListPresenter: GoodListContract.Presenter
    private lateinit var goodItemAdapter: BaseAdapter<Good, GoodItemAdapter.GoodItemViewHolder>

    private lateinit var goodDetailDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var imgBarcode: ImageView
    private lateinit var tvBarcodeValue: TextView
    private lateinit var tvGoodName: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var tvUpdatedAt: TextView

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

        goodListPresenter = GoodListPresenter(this, goodRepository)

        initGoodItemAdapter()

        initTopBarFragment(
            R.string.mode_show_all_goods_title,
            R.drawable.ic_baseline_goods_24
        )

        initItemsView()

        lifecycleScope.launch {
            goodListPresenter.loadInitialList()
        }

        layoutInflater.inflate(R.layout.good_detail_dialog_view, null).apply {
            initGoodDetailDialogBuilder(this)
            initGoodDetailDialogComponents(this)
        }
    }

    private fun initGoodItemAdapter() {
        goodItemAdapter = GoodItemAdapter({ good -> onItemClick(good) }, this)
        val itemTouchHelper = ItemTouchHelper(RecyclerViewItemRightSwipeCallback(goodItemAdapter))
        itemTouchHelper.attachToRecyclerView(itemsRecyclerView)
    }

    private fun initGoodDetailDialogComponents(dialogView: View) {
        imgBarcode = dialogView.findViewById(R.id.good_details_barcode_img)
        tvBarcodeValue = dialogView.findViewById(R.id.good_details_tv_barcode)
        tvCreatedAt = dialogView.findViewById(R.id.good_details_tv_created)
        tvUpdatedAt = dialogView.findViewById(R.id.good_details_tv_updated)
        tvGoodName = dialogView.findViewById(R.id.good_details_tv_name)
    }

    private fun initGoodDetailDialogBuilder(dialogView: View) {
        goodDetailDialogBuilder = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setNegativeButton(R.string.hide_text) { dialog, _ ->
                dialog.dismiss()
            }
    }

    private fun onItemClick(selectedGood: Good) {
        configureDialogBarcodeImage(selectedGood)

        tvBarcodeValue.apply {
            text = selectedGood.barcode
        }

        tvCreatedAt.apply {
            text = getString(R.string.created_at_template_string, selectedGood.createdAtFormatted)
        }

        configureDialogGoodUpdatedAt(selectedGood)
        configureDialogGoodName(selectedGood)

        goodDetailDialogBuilder.show()
    }

    private fun configureDialogGoodName(selectedGood: Good) {
        if (!selectedGood.name.isNullOrBlank()) {
            tvGoodName
                .apply {
                    text = getString(R.string.name_template_string, selectedGood.name)
                }.show()
        } else {
            tvGoodName.hide()
        }
    }

    private fun configureDialogGoodUpdatedAt(selectedGood: Good) {
        selectedGood.updatedAt?.let {
            tvUpdatedAt
                .apply {
                    text =
                        getString(
                            R.string.updated_at_template_string,
                            selectedGood.updatedAtFormatted
                        )
                }.show()
        } ?: run {
            tvUpdatedAt.hide()
        }
    }

    private fun configureDialogBarcodeImage(selectedGood: Good) {
        val barcodeBitmap = createBitmapFromGood(selectedGood)
        imgBarcode.setImageBitmap(barcodeBitmap)
        barcodeBitmap?.let {
            imgBarcode.setImageBitmap(barcodeBitmap)
        }
    }

    private fun initItemsView() {
        itemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@GoodListActivity)
            adapter = goodItemAdapter
            setHasFixedSize(true)
        }
    }

    override fun populateItems(goods: List<Good>) {
        goodItemAdapter.updateItems(goods)
    }

    override fun removeItem(good: Good) {
        lifecycleScope.launch {
            goodRepository.remove(good)
        }
    }
}