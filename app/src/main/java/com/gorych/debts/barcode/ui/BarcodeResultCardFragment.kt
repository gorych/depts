package com.gorych.debts.barcode.ui

import android.content.DialogInterface
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.barcode.BarcodeResultCard
import com.gorych.debts.camera.WorkflowModel
import com.gorych.debts.camera.WorkflowModel.WorkflowState
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.good.Good
import com.gorych.debts.good.dao.GoodDao
import com.gorych.debts.utility.BitmapUtils.convertBytesToBitmap
import com.gorych.debts.utility.BitmapUtils.createBitmapFromGood
import com.gorych.debts.utility.ToastUtils.Companion.toast
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.show
import com.gorych.debts.utility.textAsString
import kotlinx.coroutines.launch

class BarcodeResultCardFragment : BottomSheetDialogFragment() {

    private lateinit var titleTextView: TextView

    private lateinit var goodImgView: ImageView
    private lateinit var goodNameTextInput: TextInputEditText
    private lateinit var goodNameTextInputLayout: TextInputLayout

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
        goodNameTextInput = view.findViewById(R.id.barcode_result_card_txt_good_name)
        goodNameTextInputLayout = view.findViewById(R.id.barcode_result_card_txt_layout_good_name)

        titleTextView = view.findViewById(R.id.barcode_result_card_tv_title)
        secondaryTextView = view.findViewById(R.id.barcode_result_card_tv_secondary_text)
        supportingTextView = view.findViewById(R.id.barcode_result_card_tv_supporting_text)

        addGoodBtn = view.findViewById(R.id.barcode_result_card_btn_add)
        okBtn = view.findViewById<MaterialButton?>(R.id.barcode_result_card_btn_ok)
            .apply { setOnClickListener { dismiss() } }

        val barcodeResultCard: BarcodeResultCard? =
            arguments?.getParcelable(ARG_BARCODE_RESULT_CARD)

        //TODO Refactor to MVP
        barcodeResultCard?.let { card ->
            titleTextView.text = card.barcodeRawValue

            val associatedGood: Good? = card.associatedGood
            associatedGood?.let { good ->
                goodImgView.setImageBitmap(createBitmapFromGood(good))
                goodNameTextInputLayout.hide()

                secondaryTextView.text = good.name
                secondaryTextView.show()

                supportingTextView.text = good.createdAt.toString()
                supportingTextView.show()

                addGoodBtn.hide()
                okBtn.show()
            } ?: run {
                goodImgView.setImageBitmap(convertBytesToBitmap(card.imgData))
                goodNameTextInputLayout.show()

                secondaryTextView.hide()
                supportingTextView.hide()

                addGoodBtn.show()
                addGoodBtn.setOnClickListener { onClickAddGoodBtn(card) }
                okBtn.hide()
            }
        }

        return view
    }

    private fun onClickAddGoodBtn(card: BarcodeResultCard) {
        lifecycleScope.launch {
            val name = goodNameTextInput.textAsString()
            if (name.isNotEmpty()) {
                name[0].uppercaseChar()
            }
            goodRepository.add(
                Good.of(card, name)
            )
        }
        toast(R.string.good_added_text)
        dismiss()
    }

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
