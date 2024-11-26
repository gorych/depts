package com.gorych.debts.barcode.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.internal.Objects
import com.google.android.material.chip.Chip
import com.google.mlkit.vision.barcode.common.Barcode
import com.gorych.debts.R
import com.gorych.debts.barcode.BarcodeProcessor
import com.gorych.debts.barcode.BarcodeResultCard
import com.gorych.debts.barcode.contract.BarcodeDetectionContract
import com.gorych.debts.barcode.presenter.BarcodePresenter
import com.gorych.debts.camera.CameraSource
import com.gorych.debts.camera.CameraSourcePreview
import com.gorych.debts.camera.GraphicOverlay
import com.gorych.debts.camera.WorkflowModel
import com.gorych.debts.camera.WorkflowModel.WorkflowState
import com.gorych.debts.config.db.AppDatabase
import com.gorych.debts.good.Good
import com.gorych.debts.good.repository.GoodRepository
import com.gorych.debts.settings.ui.SettingsActivity
import com.gorych.debts.utility.hide
import com.gorych.debts.utility.isGone
import com.gorych.debts.utility.isVisible
import com.gorych.debts.utility.show
import kotlinx.coroutines.launch
import java.io.IOException

class LiveBarcodeScanningActivity : AppCompatActivity(), OnClickListener,
    BarcodeDetectionContract.View {

    private var workflowModel: WorkflowModel? = null
    private var currentWorkflowState: WorkflowState? = null

    private var cameraSource: CameraSource? = null
    private lateinit var graphicOverlay: GraphicOverlay
    private lateinit var preview: CameraSourcePreview

    private lateinit var settingsButton: View
    private lateinit var flashButton: View
    private lateinit var promptChip: Chip
    private lateinit var promptChipAnimator: AnimatorSet

    private lateinit var barcodePresenter: BarcodeDetectionContract.Presenter

    private val goodRepository: GoodRepository by lazy {
        val database = AppDatabase.getDatabase(applicationContext)
        GoodRepository(database.goodDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        barcodePresenter = BarcodePresenter(this, goodRepository)

        setContentView(R.layout.activity_live_barcode)
        preview = findViewById(R.id.barcode_csp_camera_preview)
        initGraphicOverlay()
        initPromptChip()
        initCameraPreviewTopBarButtons()
        setUpWorkflowModel()
    }

    override fun onResume() {
        super.onResume()

        workflowModel?.markCameraFrozen()
        settingsButton.isEnabled = true
        currentWorkflowState = WorkflowState.NOT_STARTED
        cameraSource?.setFrameProcessor(
            BarcodeProcessor(
                graphicOverlay,
                workflowModel!!
            )
        )
        workflowModel?.setWorkflowState(WorkflowState.DETECTING)
    }

    override fun onPostResume() {
        super.onPostResume()
        BarcodeResultCardFragment.dismiss(supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.barcode_iv_close_button -> onBackPressed()
            R.id.barcode_iv_flash_button -> onFlashButtonClicked()
            R.id.barcode_iv_settings_button -> onSettingsButtonClick()
        }
    }

    override fun refreshWhenWorkflowStatusIsDetecting() {
        promptChip.show()
        promptChip.setText(R.string.prompt_point_at_a_barcode)
        startCameraPreview()
    }

    override fun refreshWhenWorkflowStatusIsConfirming() {
        promptChip.show()
        promptChip.setText(R.string.prompt_move_camera_closer)
        startCameraPreview()
    }

    override fun refreshWhenWorkflowStatusIsSearching() {
        promptChip.show()
        promptChip.setText(R.string.prompt_searching)
        stopCameraPreview()
    }

    override fun refreshWhenWorkflowStatusIsDetected() {
        promptChip.hide()
        stopCameraPreview()
    }

    private fun initGraphicOverlay() {
        graphicOverlay = findViewById<GraphicOverlay>(R.id.camera_preview_graphic_overlay).apply {
            setOnClickListener(this@LiveBarcodeScanningActivity)
            cameraSource = CameraSource(this)
        }
    }

    private fun initPromptChip() {
        promptChip = findViewById(R.id.camera_preview_chip_bottom_prompt)
        promptChipAnimator =
            (AnimatorInflater.loadAnimator(
                this,
                R.animator.bottom_prompt_chip_enter
            ) as AnimatorSet).apply {
                setTarget(promptChip)
            }
    }

    private fun initCameraPreviewTopBarButtons() {
        findViewById<View>(R.id.barcode_iv_close_button).setOnClickListener(this)

        flashButton = findViewById<View>(R.id.barcode_iv_flash_button).apply {
            setOnClickListener(this@LiveBarcodeScanningActivity)
        }

        settingsButton = findViewById<View>(R.id.barcode_iv_settings_button).apply {
            setOnClickListener(this@LiveBarcodeScanningActivity)
        }
    }

    private fun startCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        val cameraSource = this.cameraSource ?: return
        if (!workflowModel.isCameraLive) {
            try {
                workflowModel.markCameraLive()
                preview.start(cameraSource)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to start camera preview!", e)
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        if (workflowModel.isCameraLive) {
            workflowModel.markCameraFrozen()
            flashButton.isSelected = false
            preview.stop()
        }
    }

    private fun setUpWorkflowModel() {
        workflowModel = ViewModelProviders.of(this)[WorkflowModel::class.java]

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel!!.workflowState.observe(this, Observer { workflowState ->
            if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                return@Observer
            }

            currentWorkflowState = workflowState
            Log.d(TAG, "Current workflow state: ${currentWorkflowState!!.name}")

            when (workflowState) {
                WorkflowState.DETECTING -> refreshWhenWorkflowStatusIsDetecting()
                WorkflowState.CONFIRMING -> refreshWhenWorkflowStatusIsConfirming()
                WorkflowState.SEARCHING -> refreshWhenWorkflowStatusIsSearching()
                WorkflowState.DETECTED, WorkflowState.SEARCHED -> refreshWhenWorkflowStatusIsDetected()
                else -> promptChip.hide()
            }

            playPromptChipAnimator()
        })

        processDetectedBarcodes()
    }

    private fun onFlashButtonClicked() {
        flashButton.let {
            if (it.isSelected) {
                it.isSelected = false
                cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
            } else {
                it.isSelected = true
                cameraSource!!.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
            }
        }
    }

    private fun onSettingsButtonClick() {
        settingsButton.isEnabled = false
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun playPromptChipAnimator() {
        val shouldPlayPromptChipEnteringAnimation = promptChip.isGone() && promptChip.isVisible()
        promptChipAnimator.let {
            if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
        }
    }

    override fun processDetectedBarcodes() {
        workflowModel?.detectedBarcode?.observe(this) { barcodeData ->
            barcodeData.let {
                lifecycleScope.launch {
                    barcodePresenter.processDetectedBarcode(barcodeData)
                }
            }
        }
    }

    override fun showBarcodeDetectionResult(barcodeData: Pair<Barcode, ByteArray>, good: Good?) {
        BarcodeResultCardFragment.show(
            supportFragmentManager,
            BarcodeResultCard.of(barcodeData, good)
        )
    }

    companion object {
        private const val TAG = "LiveBarcodeActivity"
    }
}
