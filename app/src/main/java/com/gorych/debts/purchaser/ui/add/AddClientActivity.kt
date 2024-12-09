package com.gorych.debts.purchaser.ui.add

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gorych.debts.R
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.core.activity.TopBarActivityBase
import com.gorych.debts.core.validation.TextInputValidator
import com.gorych.debts.core.watcher.AbstractOnTextChangedWatcher
import com.gorych.debts.core.watcher.OnTextChangedWatcher
import com.gorych.debts.home.MainActivity
import com.gorych.debts.purchaser.IntentExtras
import com.gorych.debts.purchaser.Purchaser
import com.gorych.debts.purchaser.repository.PurchaserRepository
import com.gorych.debts.purchaser.validation.FirstNameValidator
import com.gorych.debts.purchaser.validation.LastNameValidator
import com.gorych.debts.purchaser.validation.PhoneValidator
import com.gorych.debts.utility.PhoneNumberUtils
import com.gorych.debts.utility.clearText
import com.gorych.debts.utility.textAsString
import kotlinx.coroutines.launch

class AddClientActivity : TopBarActivityBase() {

    private lateinit var addBtn: MaterialButton

    private lateinit var inputFirstName: TextInputEditText
    private lateinit var inputLastName: TextInputEditText
    private lateinit var inputPhone: TextInputEditText

    private lateinit var layoutPhoneInput: TextInputLayout

    private lateinit var inputFields: List<TextInputEditText>
    private var inputFieldValidators: MutableList<TextInputValidator> = mutableListOf()

    private lateinit var previousActivityClassName: String

    private val purchaserRepository: PurchaserRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        PurchaserRepository(database.purchaserDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_purchaser)

        val view = findViewById<View>(R.id.add_purchaser)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initTopBarFragment(
            getString(R.string.add_client_activity_title),
            R.drawable.ic_person_add_24
        )

        initInputFields(view)

        previousActivityClassName =
            intent.getStringExtra(IntentExtras.PREVIOUS_ACTIVITY_NAME)
                ?: MainActivity::class.java.name

        addBtn = view.findViewById<MaterialButton?>(R.id.add_purchaser_btn_add).apply {
            setOnClickListener {
                val notValidFields = inputFieldValidators.filterNot { it.isValid() }
                if (notValidFields.isEmpty()) {
                    savePurchaser()
                    showQuestionDialog()
                } else {
                    notValidFields.forEach { it.showError() }
                }
            }
        }
    }

    private fun initInputFields(view: View) {
        initFirstNameComponents(view)
        initLastNameComponents(view)
        initPhoneComponents(view)

        inputFields = listOf(inputFirstName, inputLastName, inputPhone)
    }

    private fun initPhoneComponents(view: View) {
        inputPhone = view.findViewById(R.id.add_purchaser_input_phone)
        inputPhone.addTextChangedListener(object : AbstractOnTextChangedWatcher() {
            private var isFormatting: Boolean = false

            override fun afterTextChanged(text: Editable?) {
                if (isFormatting) {
                    return
                }
                isFormatting = true

                val formattedPhoneString = PhoneNumberUtils.format(text)
                inputPhone.setText(formattedPhoneString)
                inputPhone.setSelection(formattedPhoneString.length)

                isFormatting = false
            }
        })
        layoutPhoneInput = view.findViewById(R.id.add_purchaser_layout_input_phone)

        val phoneValidator: TextInputValidator =
            PhoneValidator(inputPhone, layoutPhoneInput, this)
        inputFieldValidators.add(phoneValidator)

        inputPhone.addTextChangedListener(OnTextChangedWatcher(phoneValidator))
    }

    private fun initLastNameComponents(view: View) {
        inputLastName = view.findViewById(R.id.add_purchaser_input_last_name)
        val layoutLastNameInput: TextInputLayout =
            view.findViewById(R.id.add_purchaser_layout_input_last_name)

        val lastNameValidator: TextInputValidator =
            LastNameValidator(inputLastName, layoutLastNameInput, this)
        inputFieldValidators.add(lastNameValidator)

        inputLastName.addTextChangedListener(OnTextChangedWatcher(lastNameValidator))
    }

    private fun initFirstNameComponents(view: View) {
        inputFirstName = view.findViewById(R.id.add_purchaser_input_first_name)
        val layoutFirstNameInput: TextInputLayout =
            view.findViewById(R.id.add_purchaser_layout_input_first_name)

        val firstNameValidator: TextInputValidator =
            FirstNameValidator(inputFirstName, layoutFirstNameInput, this)
        inputFieldValidators.add(firstNameValidator)

        inputFirstName.addTextChangedListener(OnTextChangedWatcher(firstNameValidator))
    }

    private fun savePurchaser() {
        lifecycleScope.launch {
            purchaserRepository.add(
                Purchaser(
                    inputFirstName.textAsString(),
                    inputLastName.textAsString(),
                    getPhoneNumber()
                )
            )
        }
    }

    private fun getPhoneNumber(): String? {
        val phone = inputPhone.text
        return when {
            phone.isNullOrEmpty() -> null
            else -> "${layoutPhoneInput.prefixText}$phone"
        }
    }

    private fun showQuestionDialog() {
        MaterialAlertDialogBuilder(this@AddClientActivity)
            .setTitle(R.string.purchaser_added_txt)
            .setMessage(R.string.add_one_more_purchaser_question)
            .setNegativeButton(R.string.no) { _, _ ->
                this.startActivity(
                    Intent(this, Class.forName(previousActivityClassName))
                )
            }
            .setPositiveButton(R.string.yes) { dialog, _ ->
                inputFields.forEach(TextInputEditText::clearText)
                inputFieldValidators.forEach { it.clearError() }
                dialog.dismiss()
            }
            .show()
    }
}