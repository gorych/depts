package com.gorych.debts.settings

import android.hardware.Camera
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.gorych.debts.R
import com.gorych.debts.camera.CameraSource
import com.gorych.debts.utility.Utils
import java.util.HashMap

/** Configures App settings.  */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setUpRearCameraPreviewSizePreference()
    }

    private fun setUpRearCameraPreviewSizePreference() {
        val previewSizePreference =
            findPreference<ListPreference>(getString(R.string.pref_key_rear_camera_preview_size))!!

        var camera: Camera? = null

        try {
            camera = Camera.open(CameraSource.CAMERA_FACING_BACK)
            val previewSizeList = Utils.generateValidPreviewSizeList(camera!!)
            val previewSizeStringValues = arrayOfNulls<String>(previewSizeList.size)
            val previewToPictureSizeStringMap = HashMap<String, String>()
            for (i in previewSizeList.indices) {
                val sizePair = previewSizeList[i]
                previewSizeStringValues[i] = sizePair.preview.toString()
                if (sizePair.picture != null) {
                    previewToPictureSizeStringMap[sizePair.preview.toString()] = sizePair.picture.toString()
                }
            }
            previewSizePreference.entries = previewSizeStringValues
            previewSizePreference.entryValues = previewSizeStringValues
            previewSizePreference.summary = previewSizePreference.entry
            previewSizePreference.setOnPreferenceChangeListener { _, newValue ->
                val newPreviewSizeStringValue = newValue as String
                val context = activity ?: return@setOnPreferenceChangeListener false
                previewSizePreference.summary = newPreviewSizeStringValue
                PreferenceUtils.saveStringPreference(
                    context,
                    R.string.pref_key_rear_camera_picture_size,
                    previewToPictureSizeStringMap[newPreviewSizeStringValue]
                )
                true
            }
        } catch (e: Exception) {
            // If there's no camera for the given camera id, hide the corresponding preference.
            previewSizePreference.parent?.removePreference(previewSizePreference)
        } finally {
            camera?.release()
        }
    }
}
