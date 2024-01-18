package com.ravimhzn.androidmockserver.utils

import android.content.SharedPreferences

object Pref {
    private val PREFERENCES_NAME = java.lang.String.format(
        "%s.%s",
        Util.context().applicationInfo.packageName,
        Util.context().applicationInfo.name
    )
    private val SHARED_PREFERENCES: SharedPreferences =
        Util.context().getSharedPreferences(
            PREFERENCES_NAME, 0
        )

    fun string(key: String?): String? {
        return SHARED_PREFERENCES.getString(key, null)
    }

    fun string(key: String?, value: String?) {
        val editor: SharedPreferences.Editor = SHARED_PREFERENCES.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun remove(key: String?) {
        val editor: SharedPreferences.Editor = SHARED_PREFERENCES.edit()
        editor.remove(key)
        editor.apply()
    }

    fun reset() {
        val editor: SharedPreferences.Editor = SHARED_PREFERENCES.edit()
        editor.clear()
        editor.apply()
    }
}