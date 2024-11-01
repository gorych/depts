package com.gorych.debts

import android.app.Application
import com.gorych.debts.utility.ToastUtils

class DebtsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        ToastUtils.initialize(this)
    }
}