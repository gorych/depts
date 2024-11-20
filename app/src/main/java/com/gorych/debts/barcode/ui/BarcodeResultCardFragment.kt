package com.gorych.debts.barcode.ui

import android.content.DialogInterface
import android.graphics.Bitmap
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
import java.time.LocalDateTime.now

class BarcodeResultCardFragment : BottomSheetDialogFragment() {

    private lateinit var barcodeImgView: ImageView

    private lateinit var goodNameTextInput: TextInputEditText
    private lateinit var goodNameTextInputLayout: TextInputLayout

    private lateinit var titleTextView: TextView
    private lateinit var secondaryTextView: TextView
    private lateinit var supportingTextView: TextView

    private lateinit var addGoodBtn: MaterialButton
    private lateinit var okBtn: MaterialButton
    private lateinit var updateBtn: MaterialButton
    private lateinit var cardActionButtons: List<MaterialButton>

    private val goodRepository: GoodDao by lazy {
        val database = AppDatabase.getDatabase(requireContext())
        database.goodDao()
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.barcode_result_sheet, viewGroup)

        initTextInputs(view)
        initTextViews(view)
        initActionButtons(view)

        barcodeImgView = view.findViewById(R.id.barcode_result_card_img)
        val barcodeResultCard: BarcodeResultCard? =
            arguments?.getParcelable(ARG_BARCODE_RESULT_CARD)

        //TODO Refactor to MVP
        barcodeResultCard?.let { card ->
            titleTextView.text = card.barcodeRawValue

            val associatedGood: Good? = card.associatedGood
            associatedGood?.let { good ->
                val barcodeBitmap = createBitmapFromGood(good)
                updateBarcodeImageView(barcodeBitmap)

                goodNameTextInputLayout.hide()

                secondaryTextView.text = good.createdAt.toString()
                secondaryTextView.show()

                if (good.name.isNullOrEmpty()) {
                    goodNameTextInputLayout.hint =
                        getString(R.string.barcode_result_card_txt_input_good_name_update_hint)
                    goodNameTextInputLayout.show()

                    supportingTextView.hide()

                    updateBtn.setOnClickListener { onClickUpdateGoodBtn(good) }
                    hideActionButtonsExceptOf(updateBtn)
                } else {
                    goodNameTextInputLayout.hide()

                    supportingTextView.text = good.name
                    supportingTextView.show()

                    hideActionButtonsExceptOf(okBtn)
                }
            } ?: run {
                val barcodeBitmap = convertBytesToBitmap(card.imgData)
                updateBarcodeImageView(barcodeBitmap)

                goodNameTextInputLayout.show()

                secondaryTextView.hide()
                supportingTextView.hide()

                addGoodBtn.setOnClickListener { onClickAddGoodBtn(card) }
                hideActionButtonsExceptOf(addGoodBtn)
            }
        }

        return view
    }

    private fun initTextInputs(view: View) {
        goodNameTextInput = view.findViewById(R.id.barcode_result_card_txt_good_name)
        goodNameTextInputLayout =
            view.findViewById<TextInputLayout?>(R.id.barcode_result_card_txt_layout_good_name)
                .apply { hint = getString(R.string.barcode_result_card_txt_input_good_name_hint) }
    }

    private fun initTextViews(view: View) {
        titleTextView = view.findViewById(R.id.barcode_result_card_tv_title)
        secondaryTextView = view.findViewById(R.id.barcode_result_card_tv_secondary_text)
        supportingTextView = view.findViewById(R.id.barcode_result_card_tv_supporting_text)
    }

    private fun initActionButtons(view: View) {
        addGoodBtn = view.findViewById(R.id.barcode_result_card_btn_add)
        okBtn = view.findViewById<MaterialButton?>(R.id.barcode_result_card_btn_ok)
            .apply { setOnClickListener { dismiss() } }
        updateBtn = view.findViewById(R.id.barcode_result_card_btn_update)
        cardActionButtons = listOf(addGoodBtn, okBtn, updateBtn)
    }

    private fun updateBarcodeImageView(barcodeBitmap: Bitmap?) {
        barcodeImgView.setImageBitmap(barcodeBitmap)
        barcodeBitmap?.let {
            barcodeImgView.setImageBitmap(barcodeBitmap)
            barcodeImgView.layoutParams.height = barcodeBitmap.height
        }
    }

    private fun hideActionButtonsExceptOf(excludedBtn: MaterialButton) {
        cardActionButtons
            .filterNot { it == excludedBtn }
            .forEach { it.hide() }
        excludedBtn.show()
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

    private fun onClickUpdateGoodBtn(existingGood: Good) {
        lifecycleScope.launch {
            val name = goodNameTextInput.textAsString()
            if (name.isNotEmpty()) {
                name[0].uppercaseChar()
            }
            goodRepository.update(
                existingGood.copy(name = name, updatedAt = now())
            )
        }
        toast(R.string.good_updated_text)
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
