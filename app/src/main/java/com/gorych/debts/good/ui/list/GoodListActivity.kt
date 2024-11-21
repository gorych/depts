package com.gorych.debts.good.ui.list

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gorych.debts.R
import com.gorych.debts.TopBarActivityBase
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.good.Good
import com.gorych.debts.good.contract.GoodListContract
import com.gorych.debts.good.presenter.GoodListPresenter
import com.gorych.debts.good.repository.GoodRepository
import com.gorych.debts.utility.BitmapUtils.createBitmapFromGood
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

        goodItemAdapter = GoodItemAdapter { good -> onItemClick(good) }
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

    private fun onItemClick(selectedGood: Good) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setNegativeButton(R.string.hide_text) { dialog, _ ->
                dialog.dismiss()
            }

        val dialogView: View = layoutInflater.inflate(R.layout.good_detail_dialog_view, null);
        dialog.setView(dialogView)

        val barcodeImgView: ImageView = dialogView.findViewById(R.id.good_details_barcode_img)
        val barcodeBitmap = createBitmapFromGood(selectedGood)
        barcodeImgView.setImageBitmap(barcodeBitmap)
        barcodeBitmap?.let {
            barcodeImgView.setImageBitmap(barcodeBitmap)
            barcodeImgView.layoutParams.height = barcodeBitmap.height
        }

        dialogView.findViewById<TextView?>(R.id.good_details_tv_barcode).apply {
            text = selectedGood.barcode
        }

        dialogView.findViewById<TextView?>(R.id.good_details_tv_created).apply {
            text = "Cоздан: " + selectedGood.createdAtFormatted
        }

        selectedGood.updatedAt?.let {
            dialogView.findViewById<TextView?>(R.id.good_details_tv_updated).apply {
                text = "Обновлен: " + selectedGood.updatedAtFormatted
            }
        }

        if (!selectedGood.name.isNullOrBlank()) {
            dialogView.findViewById<TextView?>(R.id.good_details_tv_name).apply {
                text = "Наименование: " + selectedGood.name
            }
        }

        dialog.show()
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