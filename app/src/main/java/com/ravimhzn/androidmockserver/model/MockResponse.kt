package com.ravimhzn.androidmockserver.model

import android.text.TextUtils
import android.util.Log
import com.ravimhzn.androidmockserver.utils.Pref
import com.ravimhzn.androidmockserver.utils.Util
import com.ravimhzn.androidmockserver.utils.Util.sortResourceFileName
import java.io.Serializable

data class MockResponse(
    val name: String,
    val path: String,
    val httpMethod: HttpMethod
) : Serializable {
    private val responseItemList = ArrayList<ResponseItem>()
    private var selected: ResponseItem? = null
    private val requestParams: Map<String, String>? = null //Todo

    fun getResponseItemList(): ArrayList<ResponseItem> {
        return responseItemList
    }

    fun getSelected(): ResponseItem? {
        return selected
    }

    fun setSelected(selected: String): MockResponse? {
        return findByString(selected)?.let { setSelected(it) }
    }

    fun setSelected(selected: ResponseItem?): MockResponse {
        this.selected = selected
        Pref.string(path, selected?.name)
        return this
    }

    fun setDefaultSelected(defaultSelected: String): MockResponse {
        val stored = Pref.string(path)
        val item = findByString((if (TextUtils.isEmpty(stored)) defaultSelected else stored))
        return setSelected((item ?: findByString(defaultSelected)))
    }

    fun getSelectedPosition(): Int {
        var pos = 0
        for (i in responseItemList.indices) {
            if (responseItemList[i].name.equals(selected?.name, true)) {
                pos = i
                break
            }
        }
        return pos
    }

    //Todo
    fun getRequestParams(): Map<String, String>? {
        return requestParams
    }

    private fun findByString(key: String?): ResponseItem? {
        var item: ResponseItem? = null
        for (inList in responseItemList) {
            if (inList.name.equals(key, true)) {
                item = inList
                break
            }
        }
        return item
    }

    fun addResponse(
        itemName: String,
        resource: Int
    ): MockResponse {
        responseItemList.add(ResponseItem(itemName, resource))
        Log.d("resource added:", resource.toString())
        return this
    }

    fun addResponses(vararg resource: Int, defaultSelected: Int = 0): MockResponse {
        val fileNames = arrayListOf<String>()
        resource.forEach {
            val fileName = Util.getResourceFileName(it).sortResourceFileName()
            fileNames.add(fileName)
            responseItemList.add(ResponseItem(fileName, it))
        }
        setDefaultSelected(fileNames[defaultSelected])
        Log.d("resource added:", resource.toString())
        return this
    }

}

enum class HttpMethod {
    GET, POST, PUT
}