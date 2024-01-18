package com.ravimhzn.androidmockserver.model

import android.content.Context
import android.content.res.Resources
import androidx.annotation.RawRes
import java.io.Serializable

data class ResponseItem(val name: String, @field:RawRes val json: Int) : Serializable {

    fun getRawResFileName(context: Context): String {
        val notFound = "Json File Not Found"
        return try {
            val fullResourceName = context.resources.getResourceName(json)
            val index = fullResourceName.lastIndexOf('/')
            if (index != -1 && index < fullResourceName.length - 1) {
                "R.raw.${fullResourceName.substring(index + 1)}"
            } else {
                notFound
            }
        } catch (e: Resources.NotFoundException) {
            notFound
        }
    }

}