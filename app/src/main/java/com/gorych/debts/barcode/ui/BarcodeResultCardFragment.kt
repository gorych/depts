package com.gorych.debts.barcode.ui

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.barcode.BarcodeResultCard
import com.gorych.debts.barcode.contract.BarcodeResultContract
import com.gorych.debts.camera.WorkflowModel
import com.gorych.debts.camera.WorkflowModel.WorkflowState
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.validation.TextInputValidator
import com.gorych.debts.core.watcher.OnTextChangedWatcher
import com.gorych.debts.good.Good
import com.gorych.debts.good.Good.MeasurementUnit
import com.gorych.debts.good.repository.GoodRepository
import com.gorych.debts.good.validation.MeasurementUnitValidator
import com.gorych.debts.core.validation.EmptyTextOrValidLengthValidator
import com.gorych.debts.utility.BitmapUtils.convertBytesToBitmap
import com.gorych.debts.utility.BitmapUtils.createBitmapFromGood
import com.gorych.debts.utility.ToastUtils.Companion.toast
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.show
import com.gorych.debts.utility.textAsString
import kotlinx.coroutines.launch
import java.time.LocalDateTime.now

class BarcodeResultCardFragment : BottomSheetDialogFragment(), BarcodeResultContract.View {

    private lateinit var barcodeImgView: ImageView
    private lateinit var goodExistenceChip: Chip

    private lateinit var goodNameTextInput: TextInputEditText
    private lateinit var goodNameTextInputLayout: TextInputLayout

    private lateinit var goodMeasurementUnitDropdown: AutoCompleteTextView
    private lateinit var goodMeasurementUnitDropdownLayout: TextInputLayout
    private lateinit var goodMeasurementUnitDropdownItems: List<GoodUnitDropdownItem>

    private var goodInputLayouts: MutableList<TextInputLayout> = mutableListOf()
    private var inputFieldValidators: MutableList<TextInputValidator> = mutableListOf()

    private lateinit var titleTextView: TextView
    private lateinit var secondaryTextView: TextView
    private lateinit var supportingLine1TextView: TextView
    private lateinit var secondLevelTextViews: List<TextView>

    private lateinit var supportingLine2TextView: TextView
    private lateinit var addGoodBtn: MaterialButton
    private lateinit var okBtn: MaterialButton
    private lateinit var updateBtn: MaterialButton
    private lateinit var cardActionButtons: List<MaterialButton>

    private val goodRepository: GoodRepository by lazy {
        val database = AppDatabase.getDatabase(requireContext())
        GoodRepository(database.goodDao())
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?,
    ): View {
        val view = layoutInflater.inflate(R.layout.barcode_result_sheet, viewGroup)

        barcodeImgView = view.findViewById(R.id.barcode_result_card_img)
        goodExistenceChip = view.findViewById(R.id.barcode_result_card_chip_good_existence)

        initTextInputs(view)
        initMeasurementUnitDropdown(view)

        initTextViews(view)
        initActionButtons(view)

        configureBarcodeResultCard()

        return view
    }

    //region Components initialization

    private fun initTextInputs(view: View) {
        goodNameTextInput = view.findViewById(R.id.barcode_result_card_txt_good_name)

        goodNameTextInputLayout =
            view.findViewById<TextInputLayout>(R.id.barcode_result_card_txt_layout_good_name)
                .apply { hint = getString(R.string.barcode_result_card_txt_input_good_name_hint) }
        goodInputLayouts.add(goodNameTextInputLayout)

        val goodTextLengthValidator =
            EmptyTextOrValidLengthValidator(goodNameTextInput, goodNameTextInputLayout, requireContext())
        inputFieldValidators.add(goodTextLengthValidator)
        goodNameTextInput.addTextChangedListener(OnTextChangedWatcher(goodTextLengthValidator))
    }

    private fun initMeasurementUnitDropdown(view: View) {
        goodMeasurementUnitDropdownItems = MeasurementUnit.entries
            .map { GoodUnitDropdownItem(it, getString(it.stringResourceId())) }
            .toList()

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.view_dropdown_item,
            R.id.dropdown_item,
            goodMeasurementUnitDropdownItems
        )

        goodMeasurementUnitDropdownLayout =
            view.findViewById(R.id.barcode_result_card_txt_layout_good_measurement_unit)
        goodInputLayouts.add(goodMeasurementUnitDropdownLayout)

        goodMeasurementUnitDropdown =
            goodMeasurementUnitDropdownLayout.editText as AutoCompleteTextView
        goodMeasurementUnitDropdown.setAdapter(adapter)

        val unitValidator = MeasurementUnitValidator(
            goodMeasurementUnitDropdown,
            goodMeasurementUnitDropdownLayout,
            requireContext()
        )
        inputFieldValidators.add(unitValidator)
        goodMeasurementUnitDropdown.addTextChangedListener(OnTextChangedWatcher(unitValidator))
    }

    private fun initTextViews(view: View) {
        titleTextView = view.findViewById(R.id.barcode_result_card_tv_title)
        secondaryTextView = view.findViewById(R.id.barcode_result_card_tv_secondary_text)
        supportingLine1TextView = view.findViewById(R.id.barcode_result_card_tv_supporting_line_1)
        supportingLine2TextView = view.findViewById(R.id.barcode_result_card_tv_supporting_line_2)

        secondLevelTextViews =
            listOf(secondaryTextView, supportingLine1TextView, supportingLine2TextView)
    }

    private fun initActionButtons(view: View) {
        addGoodBtn = view.findViewById(R.id.barcode_result_card_btn_add)
        okBtn = view.findViewById<MaterialButton?>(R.id.barcode_result_card_btn_ok)
            .apply { setOnClickListener { dismiss() } }
        updateBtn = view.findViewById(R.id.barcode_result_card_btn_update)
        cardActionButtons = listOf(addGoodBtn, okBtn, updateBtn)
    }

    //endregion

    //region Barcode result card configuration

    override fun configureBarcodeResultCard() {
        val barcodeResultCard: BarcodeResultCard? =
            arguments?.getParcelable(ARG_BARCODE_RESULT_CARD)

        barcodeResultCard?.let { card ->
            titleTextView.text = card.barcodeRawValue

            val associatedGood: Good? = card.associatedGood
            associatedGood?.let { good ->
                configureComponentsForExistingGood(good)
            } ?: run {
                configureComponentsForNotExistingGood(card)
            }
        }
    }

    private fun configureComponentsForExistingGood(good: Good) {
        val barcodeBitmap = createBitmapFromGood(good)
        updateBarcodeImageView(barcodeBitmap)

        configureGoodExistenceChip(R.drawable.ic_check_24, R.string.good_found)

        goodInputLayouts.forEach(TextInputLayout::hide)

        secondaryTextView.text =
            getString(R.string.created_at_template_string, good.createdAtFormatted)
        secondaryTextView.show()

        when {
            good.name.isNullOrEmpty() -> configureComponentsForExistingGoodWithoutName(good)
            else -> configureComponentsForExistingGoodWithName(good)
        }

        supportingLine2TextView.text = getString(
            R.string.measurement_unit_template_string,
            getString(good.measurementUnit.stringResourceId())
        )
        supportingLine2TextView.show()
    }

    private fun configureComponentsForExistingGoodWithoutName(good: Good) {
        goodNameTextInputLayout.hint =
            getString(R.string.barcode_result_card_txt_input_good_name_update_hint)
        goodNameTextInputLayout.show()

        supportingLine1TextView.hide()

        updateBtn.setOnClickListener { onClickUpdateGoodBtn(good) }
        hideActionButtonsExceptOf(updateBtn)
    }

    private fun configureComponentsForExistingGoodWithName(good: Good) {
        goodNameTextInputLayout.hide()

        supportingLine1TextView.text = getString(R.string.name_template_string, good.name)
        supportingLine1TextView.show()

        hideActionButtonsExceptOf(okBtn)
    }

    private fun configureComponentsForNotExistingGood(card: BarcodeResultCard) {
        val barcodeBitmap = convertBytesToBitmap(card.imgData)
        updateBarcodeImageView(barcodeBitmap)

        configureGoodExistenceChip(R.drawable.ic_close_24, R.string.good_not_found)

        goodInputLayouts.forEach(TextInputLayout::show)
        secondLevelTextViews.forEach(TextView::hide)

        addGoodBtn.setOnClickListener { onClickAddGoodBtn(card) }
        hideActionButtonsExceptOf(addGoodBtn)
    }

    private fun configureGoodExistenceChip(iconResId: Int, textResId: Int) {
        goodExistenceChip.apply {
            text = getString(textResId)
            chipIcon = ResourcesCompat.getDrawable(
                context.resources,
                iconResId,
                context.theme
            )
        }
    }

    private fun updateBarcodeImageView(barcodeBitmap: Bitmap?) {
        barcodeImgView.setImageBitmap(barcodeBitmap)
        barcodeBitmap?.let {
            barcodeImgView.setImageBitmap(barcodeBitmap)
            //barcodeImgView.layoutParams.height = barcodeBitmap.height
        }
    }

    private fun hideActionButtonsExceptOf(excludedBtn: MaterialButton) {
        cardActionButtons
            .filterNot { it == excludedBtn }
            .forEach { it.hide() }
        excludedBtn.show()
    }

    //Good actions

    private fun onClickGoodActionBtn(action: () -> Unit) {
        val notValidFields = inputFieldValidators.filterNot { it.isValid() }
        if (notValidFields.isEmpty()) {
            action()
            dismiss()
        } else {
            notValidFields.forEach { it.showError() }
        }
    }

    private fun onClickAddGoodBtn(card: BarcodeResultCard) {
        onClickGoodActionBtn {
            saveGood(card)
            toast(R.string.good_added_text)
        }
    }

    private fun saveGood(card: BarcodeResultCard) {
        lifecycleScope.launch {
            goodRepository.add(
                Good.of(
                    card,
                    name = goodNameTextInput.textAsString(),
                    unit = goodMeasurementUnitDropdownItems.first {
                        it.textValue == goodMeasurementUnitDropdown.textAsString()
                    }.unit
                )
            )
        }
    }

    private fun onClickUpdateGoodBtn(existingGood: Good) {
        onClickGoodActionBtn {
            updateGood(existingGood)
            toast(R.string.good_updated_text)
        }
    }

    private fun updateGood(existingGood: Good) {
        lifecycleScope.launch {
            goodRepository.update(
                existingGood.copy(name = goodNameTextInput.textAsString(), updatedAt = now())
            )
        }
    }

    //endregion

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
            barcodeCard: BarcodeResultCard,
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
