package com.ravimhzn.androidmockserver.ui.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.ravimhzn.androidmockserver.utils.DimensionUtil.textSizeDefault
import com.ravimhzn.androidmockserver.utils.DimensionUtil.toolbarElevation
import com.ravimhzn.androidmockserver.utils.StringUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyComposeToolbar(
    appTitle: String = StringUtils.mockServer,
    displayMenu: Boolean = true,
    startServerListener: () -> Unit = {},
    stopServerListener: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = appTitle, style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = textSizeDefault
                )
            )
        },
        modifier = Modifier
            .shadow(
                elevation = toolbarElevation,
                spotColor = Color.DarkGray
            ),
        actions = {
            if(displayMenu){
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = StringUtils.contentDescMenu)

                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = StringUtils.startServer) },
                        onClick = {
                            showMenu = false
                            startServerListener.invoke()
                        })
                    DropdownMenuItem(
                        text = { Text(text = StringUtils.stopServer) },
                        onClick = {
                            showMenu = false
                            stopServerListener.invoke()
                        })
                }
            }
        },
    )
}
