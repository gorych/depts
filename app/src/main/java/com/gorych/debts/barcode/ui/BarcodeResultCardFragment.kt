package com.gorych.debts.barcode.ui

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.gorych.debts.R
import com.gorych.debts.barcode.BarcodeResultCard
import com.gorych.debts.camera.WorkflowModel
import com.gorych.debts.camera.WorkflowModel.WorkflowState
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.good.Good
import com.gorych.debts.good.dao.GoodDao
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.show
import kotlinx.coroutines.launch

class BarcodeResultCardFragment : BottomSheetDialogFragment() {

    private lateinit var goodImgView: ImageView

    private lateinit var titleTextView: TextView

    private lateinit var secondaryTextView: TextView
    private lateinit var supportingTextView: TextView

    private lateinit var addGoodBtn: MaterialButton
    private lateinit var okBtn: MaterialButton

    private val goodRepository: GoodDao by lazy {
        val database = AppDatabase.getDatabase(requireContext())
        database.goodDao()
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.barcode_result_sheet, viewGroup)

        goodImgView = view.findViewById(R.id.barcode_result_card_img)

        titleTextView = view.findViewById(R.id.barcode_result_card_tv_title)
        secondaryTextView = view.findViewById(R.id.barcode_result_card_tv_secondary_text)
        supportingTextView = view.findViewById(R.id.barcode_result_card_tv_supporting_text)

        //TODO problem after the act btns are clicked + color + img size +

        addGoodBtn = view.findViewById(R.id.barcode_result_card_btn_add)
        okBtn = view.findViewById(R.id.barcode_result_card_btn_ok)

        val barcodeResultCard: BarcodeResultCard? =
            arguments?.getParcelable(ARG_BARCODE_RESULT_CARD)

        barcodeResultCard?.let { card ->
            titleTextView.text = card.barcodeRawValue

            val associatedGood: Good? = card.associatedGood
            associatedGood?.let { good ->
                goodImgView.setImageBitmap(createBitmapFromGood(good))
                goodImgView.show()

                secondaryTextView.text = good.name
                secondaryTextView.show()

                supportingTextView.text = good.createdAt.toString()
                supportingTextView.show()

                addGoodBtn.hide()
                okBtn.show()
            } ?: run {
                goodImgView.hide()

                secondaryTextView.hide()
                supportingTextView.hide()

                addGoodBtn.show()
                addGoodBtn.setOnClickListener {
                    lifecycleScope.launch {
                        goodRepository.add(
                            //TODO add entered name
                            Good(
                                barcode = card.barcodeRawValue,
                                name = "Test Good Name 1",
                                imageData = card.imgData
                            )
                        )
                    }
                }
                okBtn.hide()
            }
        }

        return view
    }

    private fun createBitmapFromGood(goodIt: Good): Bitmap? =
        BitmapFactory.decodeByteArray(
            goodIt.imageData,
            0,
            goodIt.imageData?.size ?: 0
        )

    override fun onDismiss(dialogInterface: DialogInterface) {
        activity?.let {
            // Back to working state after the bottom sheet is dismissed.
            ViewModelProviders.of(it)[WorkflowModel::class.java].setWorkflowState(WorkflowState.DETECTING)
        }
        super.onDismiss(dialogInterface)
    }

    companion object {
        private const val TAG = "BarcodeResultFragment"
        private const val ARG_BARCODE_RESULT_CARD = "arg_barcode_result_card"

        fun show(
            fragmentManager: FragmentManager,
            barcodeCard: BarcodeResultCard
        ) {
            val barcodeResultFragment = BarcodeResultCardFragment()
            barcodeResultFragment.arguments = Bundle().apply {
                putParcelable(ARG_BARCODE_RESULT_CARD, barcodeCard)
            }
            barcodeResultFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as BarcodeResultCardFragment?)?.dismiss()
        }
    }
}
