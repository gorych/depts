package com.gorych.debts.barcode

import android.animation.ValueAnimator
import android.graphics.Rect
import android.util.Log
import androidx.annotation.MainThread
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.gorych.debts.camera.CameraReticleAnimator
import com.gorych.debts.camera.FrameProcessorBase
import com.gorych.debts.camera.GraphicOverlay
import com.gorych.debts.camera.InputInfo
import com.gorych.debts.camera.WorkflowModel
import com.gorych.debts.camera.WorkflowModel.WorkflowState
import com.gorych.debts.settings.PreferenceUtils
import com.gorych.debts.utility.BitmapUtils
import java.io.IOException

/** A processor to run the barcode detector.  */
class BarcodeProcessor(graphicOverlay: GraphicOverlay, private val workflowModel: WorkflowModel) :
    FrameProcessorBase<List<Barcode>>() {

    private val scanner = BarcodeScanning.getClient()
    private val cameraReticleAnimator: CameraReticleAnimator = CameraReticleAnimator(graphicOverlay)

    override fun detectInImage(image: InputImage): Task<List<Barcode>> =
        scanner.process(image)

    @MainThread
    override fun onSuccess(
        inputInfo: InputInfo,
        results: List<Barcode>,
        graphicOverlay: GraphicOverlay
    ) {
        if (!workflowModel.isCameraLive) return

        Log.d(TAG, "Barcode result size: ${results.size}")

        // Picks the barcode, if exists, that covers the center of graphic overlay.

        val barcodeInCenter = results.firstOrNull { barcode ->
            val boundingBox = barcode.boundingBox ?: return@firstOrNull false
            val box = graphicOverlay.translateRect(boundingBox)
            box.contains(graphicOverlay.width / 2f, graphicOverlay.height / 2f)
        }

        graphicOverlay.clear()
        if (barcodeInCenter == null) {
            cameraReticleAnimator.start()
            graphicOverlay.add(BarcodeReticleGraphic(graphicOverlay, cameraReticleAnimator))
            workflowModel.setWorkflowState(WorkflowState.DETECTING)
        } else {
            cameraReticleAnimator.cancel()
            val sizeProgress = PreferenceUtils.getProgressToMeetBarcodeSizeRequirement(
                graphicOverlay,
                barcodeInCenter
            )
            if (sizeProgress < 1) {
                // Barcode in the camera view is too small, so prompt user to move camera closer.
                graphicOverlay.add(BarcodeConfirmingGraphic(graphicOverlay, barcodeInCenter))
                workflowModel.setWorkflowState(WorkflowState.CONFIRMING)
            } else {
                // Barcode size in the camera view is sufficient.
                if (PreferenceUtils.shouldDelayLoadingBarcodeResult(graphicOverlay.context)) {
                    val loadingAnimator =
                        createLoadingAnimator(graphicOverlay, barcodeInCenter, inputInfo)
                    loadingAnimator.start()
                    graphicOverlay.add(BarcodeLoadingGraphic(graphicOverlay, loadingAnimator))
                    workflowModel.setWorkflowState(WorkflowState.SEARCHING)
                } else {
                    updateWorkflowOnSuccess(WorkflowState.DETECTED, barcodeInCenter, inputInfo)
                }
            }
        }
        graphicOverlay.invalidate()
    }

    private fun updateWorkflowOnSuccess(
        state: WorkflowState,
        detectedBarcode: Barcode,
        cameraInput: InputInfo
    ) {
        detectedBarcode.boundingBox?.let {
            val barcodeImageBytes = getBarcodeBitmapAsBytes(cameraInput, it)
            workflowModel.setWorkflowState(state)
            workflowModel.detectedBarcode.value = Pair(detectedBarcode, barcodeImageBytes)
        }
    }

    private fun getBarcodeBitmapAsBytes(
        inputInfo: InputInfo,
        barcodeBoundingBox: Rect
    ): ByteArray {
        val croppedBitmap = BitmapUtils.cropBitmap(inputInfo.getBitmap(), barcodeBoundingBox)
        return BitmapUtils.getBitmapAsBytes(croppedBitmap)
    }

    private fun createLoadingAnimator(
        graphicOverlay: GraphicOverlay,
        barcode: Barcode,
        inputInfo: InputInfo
    ): ValueAnimator {
        val endProgress = 1.1f
        return ValueAnimator.ofFloat(0f, endProgress).apply {
            duration = 2000
            addUpdateListener {
                if ((animatedValue as Float).compareTo(endProgress) >= 0) {
                    graphicOverlay.clear()
                    updateWorkflowOnSuccess(WorkflowState.SEARCHED, barcode, inputInfo)
                } else {
                    graphicOverlay.invalidate()
                }
            }
        }
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed!", e)
    }

    override fun stop() {
        super.stop()
        try {
            scanner.close()
        } catch (e: IOException) {
            Log.e(TAG, "Failed to close barcode detector!", e)
        }
    }

    companion object {
        private const val TAG = "BarcodeProcessor"
    }
}
