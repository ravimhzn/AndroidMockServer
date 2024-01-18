package com.ravimhzn.androidmockserver.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object GsonConverter {

    private val gson = Gson()
    @Throws(JsonSyntaxException::class)
    fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    fun fromJson(obj: Any): String {
        return gson.toJson(obj)
    }
}