package com.ravimhzn.androidmockserver.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ravimhzn.androidmockserver.ui.theme.AndroidMockServerTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidMockServerTheme {
                navController = rememberNavController()
                //SetUpNavGraph(navController)
            }
        }
    }
}