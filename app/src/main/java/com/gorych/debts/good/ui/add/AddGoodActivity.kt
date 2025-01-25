package com.gorych.debts.good.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.barcode.ui.GoodUnitDropdownItem
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.core.validation.TextInputValidator
import com.gorych.debts.core.watcher.OnTextChangedWatcher
import com.gorych.debts.good.Good
import com.gorych.debts.good.Good.MeasurementUnit
import com.gorych.debts.good.exception.BarcodeUniquenessException
import com.gorych.debts.good.repository.GoodRepository
import com.gorych.debts.good.validation.barcode.NotEmptyBarcodeValidator
import com.gorych.debts.good.validation.barcode.ValidBarcodeLengthValidator
import com.gorych.debts.good.validation.name.NotEmptyNameValidator
import com.gorych.debts.good.validation.name.ValidNameLengthValidator
import com.gorych.debts.good.validation.unit.MeasurementUnitValidator
import com.gorych.debts.home.MainActivity
import com.gorych.debts.core.IntentExtras
import com.gorych.debts.utility.clearText
import com.gorych.debts.utility.textAsString
import kotlinx.coroutines.launch


class AddGoodActivity : TopBarActivityBase() {

    private lateinit var inputName: TextInputEditText
    private lateinit var inputBarcode: TextInputEditText
    private lateinit var layoutBarcodeInput: TextInputLayout

    private lateinit var dropdownMeasurementUnit: AutoCompleteTextView
    private lateinit var measurementUnitDropdownLayout: TextInputLayout
    private lateinit var measurementUnitDropdownItems: List<GoodUnitDropdownItem>

    private lateinit var inputFields: List<EditText>
    private var inputFieldValidators: MutableList<TextInputValidator> = mutableListOf()

    private lateinit var previousActivityClassName: String

    private val goodRepository: GoodRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        GoodRepository(database.goodDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_good)

        val view = findViewById<View>(R.id.add_good)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initTopBarFragment(
            getString(R.string.add_good_title),
            R.drawable.ic_add_list_24
        )

        initNameComponents(view)
        initBarcodeComponents(view)
        initMeasurementUnitDropdown(view)
        inputFields = listOf(inputName, inputBarcode, dropdownMeasurementUnit)

        previousActivityClassName =
            intent.getStringExtra(IntentExtras.PREVIOUS_ACTIVITY_NAME)
                ?: MainActivity::class.java.name

        initAddGoodBtn(view)
    }

    private fun initAddGoodBtn(view: View) {
        view.findViewById<MaterialButton>(R.id.add_good_btn_add).setOnClickListener {
            val notValidFields = inputFieldValidators.filterNot { it.isValid() }
            if (notValidFields.isEmpty()) {
                saveGood()
            } else {
                notValidFields.forEach { it.showError() }
            }
        }
    }

    private fun saveGood() {
        lifecycleScope.launch {
            try {
                goodRepository.add(
                    Good(
                        name = inputName.textAsString(),
                        barcode = inputBarcode.textAsString(),
                        unit = measurementUnitDropdownItems.first {
                            it.textValue == dropdownMeasurementUnit.textAsString()
                        }.unit
                    )
                )
                showQuestionDialog()
            } catch (e: BarcodeUniquenessException) {
                layoutBarcodeInput.error = getString(R.string.barcode_uniqueness_error)
            }
        }
    }

    private fun initNameComponents(view: View) {
        inputName = view.findViewById(R.id.add_good_input_name)
        val layoutNameInput: TextInputLayout = view.findViewById(R.id.add_good_layout_input_name)

        val notEmptyNameValidator = NotEmptyNameValidator(inputName, layoutNameInput, this)
        inputName.addTextChangedListener(OnTextChangedWatcher(notEmptyNameValidator))
        inputFieldValidators.add(notEmptyNameValidator)

        val validNameLengthValidator = ValidNameLengthValidator(inputName, layoutNameInput, this)
        inputName.addTextChangedListener(OnTextChangedWatcher(validNameLengthValidator))
        inputFieldValidators.add(validNameLengthValidator)

        inputName.addTextChangedListener(OnTextChangedWatcher(notEmptyNameValidator))
    }

    private fun initBarcodeComponents(view: View) {
        inputBarcode = view.findViewById(R.id.add_good_input_barcode)
        layoutBarcodeInput = view.findViewById(R.id.add_good_layout_input_barcode)

        val notEmptyBarcodeValidator =
            NotEmptyBarcodeValidator(inputBarcode, layoutBarcodeInput, this)
        inputBarcode.addTextChangedListener(OnTextChangedWatcher(notEmptyBarcodeValidator))
        inputFieldValidators.add(notEmptyBarcodeValidator)

        val validBarcodeLengthValidator =
            ValidBarcodeLengthValidator(inputBarcode, layoutBarcodeInput, this)
        inputBarcode.addTextChangedListener(OnTextChangedWatcher(validBarcodeLengthValidator))
        inputFieldValidators.add(validBarcodeLengthValidator)
    }

    private fun initMeasurementUnitDropdown(view: View) {
        measurementUnitDropdownItems = MeasurementUnit.entries
            .map { GoodUnitDropdownItem(it, getString(it.stringResourceId())) }
            .toList()

        val adapter = ArrayAdapter(
            this,
            R.layout.view_dropdown_item,
            R.id.dropdown_item,
            measurementUnitDropdownItems
        )

        measurementUnitDropdownLayout =
            view.findViewById(R.id.add_good_layout_input_good_measurement_unit)

        dropdownMeasurementUnit =
            measurementUnitDropdownLayout.editText as AutoCompleteTextView
        dropdownMeasurementUnit.setAdapter(adapter)

        val unitValidator = MeasurementUnitValidator(
            dropdownMeasurementUnit,
            measurementUnitDropdownLayout,
            this
        )

        inputFieldValidators.add(unitValidator)
        dropdownMeasurementUnit.addTextChangedListener(object :
            OnTextChangedWatcher(unitValidator) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    !validator.isValid() -> validator.showError()
                    else -> validator.clearError()
                }
            }
        })

        dropdownMeasurementUnit.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                hideKeyboard(v);
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showQuestionDialog() {
        MaterialAlertDialogBuilder(this@AddGoodActivity)
            .setTitle(R.string.good_added_successfully_text)
            .setMessage(R.string.add_one_more_good_question)
            .setCancelable(false)
            .setNegativeButton(R.string.yes) { dialog, _ ->
                inputFields.forEach(EditText::clearText)
                inputFieldValidators.forEach { it.clearError() }
                dialog.dismiss()
            }
            .setPositiveButton(R.string.no) { _, _ ->
                this.startActivity(
                    Intent(this, Class.forName(previousActivityClassName))
                )
            }
            .show()
    }
}