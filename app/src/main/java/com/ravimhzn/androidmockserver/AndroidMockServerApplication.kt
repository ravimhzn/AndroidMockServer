package com.ravimhzn.androidmockserver

import android.app.Application
import com.ravimhzn.androidmockserver.utils.Views

class AndroidMockServerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Views.init(applicationContext)
    }
}