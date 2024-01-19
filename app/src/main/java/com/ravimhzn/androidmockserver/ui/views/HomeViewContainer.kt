package com.ravimhzn.androidmockserver.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ravimhzn.androidmockserver.model.MockResponse
import com.ravimhzn.androidmockserver.navigation.MOCK_RESPONSE_KEY
import com.ravimhzn.androidmockserver.utils.DimensionUtil.contentSpacing
import com.ravimhzn.androidmockserver.utils.DimensionUtil.contentSpacingSmall
import com.ravimhzn.androidmockserver.utils.DimensionUtil.contentSpacingTiny
import com.ravimhzn.androidmockserver.utils.DimensionUtil.textSizeBody
import com.ravimhzn.androidmockserver.utils.StringUtils.serverStartedMessage
import com.ravimhzn.androidmockserver.utils.StringUtils.serverStoppedMessage
import com.ravimhzn.androidmockserver.utils.Util.showToastMessage
import com.ravimhzn.starappmockserver.android.R
import com.ravimhzn.androidmockserver.navigation.Screen
import com.ravimhzn.androidmockserver.viewmodels.MockDataViewModel

@Composable
fun HomeViewContainer(navController: NavHostController, viewModel: MockDataViewModel) {
    val serverStatus = remember {
        mutableStateOf("Server Status")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MyComposeToolbar(startServerListener = {
                viewModel.startNettyServer()
                serverStatus.value = serverStartedMessage
                showToastMessage(serverStartedMessage)
            }, stopServerListener = {
                viewModel.stopNettyServer()
                serverStatus.value = serverStoppedMessage
                showToastMessage(serverStoppedMessage)
            })

            SetMockResponseColumn(navController, viewModel)

            Spacer(modifier = Modifier.height(contentSpacing))
        }
    }
}

@Composable
fun SetMockResponseColumn(navHostController: NavHostController, viewModel: MockDataViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val mockResponses = viewModel.getMockResponses()
    val filteredResponses = remember { mutableStateOf(mockResponses) }

    LazyColumn {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    filteredResponses.value = if (searchQuery.isEmpty()) {
                        mockResponses
                    } else {
                        mockResponses.filter {
                            it.name.contains(searchQuery, ignoreCase = true) || it.path.contains(searchQuery, ignoreCase = true)
                        }
                    }
                },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentSpacing)
            )
        }

        // Show the entire list initially
        items(filteredResponses.value.size) { index ->
            CustomListView(filteredResponses.value[index]) {
                navHostController.currentBackStackEntry?.savedStateHandle?.set(
                    key = MOCK_RESPONSE_KEY,
                    value = filteredResponses.value[index]
                )
                navHostController.navigate(Screen.Detail.route)
            }

            if (index < filteredResponses.value.size - 1) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun CustomListView(response: MockResponse? = null, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke() }
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(start = contentSpacing, top = contentSpacingSmall, bottom = contentSpacingSmall)
            ) {
                Text(
                    text = response!!.name, style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = textSizeBody
                    )
                )

                Text(
                    text = response.path,
                    modifier = Modifier.padding(top = contentSpacingTiny),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = textSizeBody
                    )
                )
            }

            Text(
                text = response!!.httpMethod.name,
                modifier = Modifier.padding(end = contentSpacing),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = textSizeBody
                )
            )

            Image(
                painter = painterResource(id = R.drawable.ic_accordion_arrow),
                contentDescription = null,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(end = contentSpacingSmall)
            )
        }
    }
}

