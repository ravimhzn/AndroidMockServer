package com.ravimhzn.androidmockserver.utils

import android.content.Context

object Views {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun context(): Context {
        return appContext
    }
}