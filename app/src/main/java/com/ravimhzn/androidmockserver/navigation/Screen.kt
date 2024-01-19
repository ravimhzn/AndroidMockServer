package com.ravimhzn.androidmockserver.navigation

import com.ravimhzn.androidmockserver.model.ResponseItem


sealed class Screen(val route: String) {
    object Home : Screen("API_HOME")
    object Detail : Screen("API_DETAIL") {
        fun setResponseItemList(itemList: ArrayList<ResponseItem>) = itemList
        fun setTestInteger(value : Int) = value
    }
}