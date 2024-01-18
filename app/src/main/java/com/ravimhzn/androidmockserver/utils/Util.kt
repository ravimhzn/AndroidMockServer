package com.ravimhzn.androidmockserver.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravimhzn.androidmockserver.model.ResponseItem
import java.io.File
import java.io.IOException
import java.io.InputStream

object Util {

    @Composable
    fun getContext() = LocalContext.current.applicationContext

    @Composable
    fun Modifier.underLine() =
        drawBehind {
            val strokeWidthPx = 1.dp.toPx()
            val verticalOffset = size.height - 2.sp.toPx()
            drawLine(
                color = Color.Gray,
                strokeWidth = strokeWidthPx,
                start = Offset(0f, verticalOffset),
                end = Offset(size.width, verticalOffset)
            )
        }

    fun showToastMessage(message: String) =
        Toast.makeText(context(), message, Toast.LENGTH_SHORT).show()

    @JvmStatic
    fun context(): Context = Views.context()

    fun jsonFileReader(response: ResponseItem): String {
        val resCode: Int = response.json
        var jsonStream: InputStream? = null
        try {
            val resources: Resources = context().resources
            jsonStream = resources.openRawResource(resCode)
            val b = ByteArray(jsonStream.available())
            jsonStream.read(b)
            return String(b)
        } catch (e: java.lang.Exception) {
            Log.e("Error", "Could not display JSON", e)
        } finally {
            try {
                jsonStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    //Todo use for UT
    fun loadResourceToString(resourceFile: String): String? {
        val classLoader = Util::class.java.classLoader
        val uri = classLoader?.getResource(resourceFile)

        return uri?.let {
            val file = File(it.path)
            String(file.readBytes())
        }
    }

    fun getResourceFileName(resourceFile: Int): String {
        val resources: Resources = context().resources
        val resourceName = resources.getResourceEntryName(resourceFile)
        return resourceName.substringAfterLast('/')
    }

    fun String.sortResourceFileName(): String {
        val replacedString = this.replace("_", " ").replace("-", " ")
        val words = replacedString.split(" ")
        val capitalizedWords =
            words.map {
                it.replaceFirstChar { it1 ->
                    if (it1.isLowerCase()) it1.titlecase() else it1.toString()
                }
            }
        return capitalizedWords.joinToString("")
    }

    fun convertDpToPx(dp: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale).toInt()
    }

    fun convertPxToDp(px: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (px / scale).toInt()
    }

    fun convertSpToPx(sp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        return (sp * scale).toInt()
    }

    fun convertPxToSp(px: Int): Float {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        return px / scale
    }

    fun isErrorResponse(json: String): Boolean {
        return json.contains("errorCode") || json.contains("error_description")
    }
}
