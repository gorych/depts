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
import com.gorych.debts.core.callback.RecyclerViewItemRightSwipeDeleteCallback
import com.gorych.debts.core.dialog.RecyclerViewItemConfirmationRemovalDialog
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

    private val goodRepository: GoodRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        GoodRepository(database.goodDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_all_goods)

        initItemsRecyclerView()

        ViewCompat.setOnApplyWindowInsetsListener(itemsRecyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        goodItemAdapter = GoodItemAdapter({ good -> onItemClick(good) }, this)

        initTopBarFragment(
            R.string.mode_show_all_goods_title,
            R.drawable.ic_item_list_24
        )

        initItemsView()

        goodListPresenter = GoodListPresenter(this, goodRepository)
        lifecycleScope.launch {
            goodListPresenter.loadInitialList()
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

    private fun initItemsRecyclerView() {
        itemsRecyclerView = findViewById(R.id.all_goods_rv_items)
        ItemTouchHelper(
            RecyclerViewItemRightSwipeDeleteCallback(
                this,
            ) { position -> showConfirmationRemovalDialog(position) }
        ).apply { attachToRecyclerView(itemsRecyclerView) }
    }

    private fun showConfirmationRemovalDialog(position: Int) {
        RecyclerViewItemConfirmationRemovalDialog(
            this,
            goodItemAdapter,
            position,
            R.string.good_removal_confirmation_text,
            R.string.good_removal_confirmation_question,
            R.string.good_removed_text
        ).show()
    }

    private fun initItemsView() {
        itemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@GoodListActivity)
            adapter = goodItemAdapter
            setHasFixedSize(true)
        }
    }

    private fun onItemClick(selectedGood: Good) {
        val dialogView = buildGoodDetailDialogView(selectedGood)
        MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setNegativeButton(R.string.hide) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    //region Good detail dialog

    private fun buildGoodDetailDialogView(selectedGood: Good): View? {
        return layoutInflater.inflate(R.layout.dialog_good_detail_view, null).also {
            configureDialogBarcodeImage(it, selectedGood)
            configureDialogBarcodeComponent(it, selectedGood)
            configureDialogGoodCreatedAtComponent(it, selectedGood)
            configureDialogGoodUpdatedAt(it, selectedGood)
            configureDialogGoodMeasurementUnit(it, selectedGood)
            configureDialogGoodName(it, selectedGood)
        }
    }

    private fun configureDialogBarcodeImage(dialogView: View, selectedGood: Good) {
        val barcodeBitmap = createBitmapFromGood(selectedGood)
        val imgBarcode: ImageView = dialogView.findViewById(R.id.good_details_barcode_img)
        imgBarcode.setImageBitmap(barcodeBitmap)
        barcodeBitmap?.let {
            imgBarcode.setImageBitmap(barcodeBitmap)
        }
    }

    private fun configureDialogGoodCreatedAtComponent(dialogView: View, selectedGood: Good) {
        val tvCreatedAt: TextView = dialogView.findViewById(R.id.good_details_tv_created)
        tvCreatedAt.apply {
            text = getString(R.string.created_at_template_string, selectedGood.createdAtFormatted)
        }
    }

    private fun configureDialogBarcodeComponent(dialogView: View, selectedGood: Good) {
        val tvBarcodeValue: TextView = dialogView.findViewById(R.id.good_details_tv_barcode)
        tvBarcodeValue.apply {
            text = selectedGood.barcode
        }
    }

    private fun configureDialogGoodName(dialogView: View, selectedGood: Good) {
        val tvGoodName: TextView = dialogView.findViewById(R.id.good_details_tv_name)
        if (!selectedGood.name.isNullOrBlank()) {
            tvGoodName
                .apply {
                    text = getString(R.string.name_template_string, selectedGood.name)
                }.show()
        } else {
            tvGoodName.hide()
        }
    }

    private fun configureDialogGoodUpdatedAt(dialogView: View, selectedGood: Good) {
        val tvUpdatedAt: TextView = dialogView.findViewById(R.id.good_details_tv_updated)
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

    private fun configureDialogGoodMeasurementUnit(dialogView: View, selectedGood: Good) {
        val tvUnit: TextView = dialogView.findViewById(R.id.good_details_tv_measurement_unit)
        selectedGood.measurementUnit.let {
            tvUnit.apply {
                text = getString(
                    R.string.measurement_unit_template_string,
                    getString(selectedGood.measurementUnit.stringResourceId())
                )
            }.show()
        }
    }

    //endregion
}