package com.gorych.debts.utility

import android.app.Application
import android.content.Context
import android.widget.Toast

class ToastUtils private constructor(application: Application) {

    private val appContext: Context = application.applicationContext

    companion object {
        private var instance: ToastUtils? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = ToastUtils(application)
            }
        }

        fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
            instance?.let {
                Toast.makeText(it.appContext, message, duration).show()
            }
        }

        fun toast(messageResourceId: Int) {
            instance?.let {
                Toast.makeText(
                    it.appContext,
                    it.appContext.getString(messageResourceId),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
