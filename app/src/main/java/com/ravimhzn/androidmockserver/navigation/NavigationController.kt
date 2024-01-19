package com.ravimhzn.androidmockserver.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ravimhzn.androidmockserver.model.MockResponse
import com.ravimhzn.androidmockserver.ui.views.ApiDetailViewContainer
import com.ravimhzn.androidmockserver.ui.views.HomeViewContainer
import com.ravimhzn.androidmockserver.viewmodels.MockDataViewModel

const val MOCK_RESPONSE_KEY = "mock_response_key"

@Composable
fun SetUpNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.Home.route, route = "root") {
        composable(Screen.Home.route) {
            val viewModel = it.sharedViewModel<MockDataViewModel>(navController)
            HomeViewContainer(navController = navController, viewModel = viewModel)
        }
        composable(
            Screen.Detail.route
        ) {
            //val result = navController.previousBackStackEntry?.savedStateHandle?.get<Person>(
            val result = navController.previousBackStackEntry?.savedStateHandle?.get<MockResponse>(
                MOCK_RESPONSE_KEY
            )
            result?.run {
                val viewModel = it.sharedViewModel<MockDataViewModel>(navController)
                viewModel.setDataToMockResponse(this)
                ApiDetailViewContainer(viewModel)
            }
            Log.d("Args", "Argument results:: ${result?.name}")
        }
    }
}


/**
 * Since it's a small application, we'll use sharedViewModel rather than individual.
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}