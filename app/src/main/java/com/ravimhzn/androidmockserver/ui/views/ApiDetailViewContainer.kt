package com.ravimhzn.androidmockserver.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravimhzn.androidmockserver.model.ResponseItem
import com.ravimhzn.androidmockserver.ui.theme.AndroidMockServerTheme
import com.ravimhzn.androidmockserver.utils.DimensionUtil.contentSpacing
import com.ravimhzn.androidmockserver.utils.DimensionUtil.contentSpacingSmall
import com.ravimhzn.androidmockserver.utils.DimensionUtil.contentSpacingTiny
import com.ravimhzn.androidmockserver.utils.DimensionUtil.textSizeBody
import com.ravimhzn.androidmockserver.utils.StringUtils
import com.ravimhzn.androidmockserver.utils.Util
import com.ravimhzn.androidmockserver.utils.Util.underLine
import com.ravimhzn.androidmockserver.viewmodels.MockDataViewModel

@Composable
fun ApiDetailViewContainer(viewModel: MockDataViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MyComposeToolbar(StringUtils.apiOptions, displayMenu = false)
            SetResponseItemList(viewModel)
        }
    }
}

@Composable
fun SetResponseItemList(viewModel: MockDataViewModel) {
    val response = viewModel.mockResponse
    val selectedResponseItem = remember { mutableIntStateOf(response.getSelectedPosition()) }
    LazyColumn {
        items(response.getResponseItemList().size) { index ->
            CheckBoxListContainer(
                response.getResponseItemList()[index],
                selectedResponseItem.intValue == index
            ) { isCheckChanged, latestSelectedItem ->
                selectedResponseItem.intValue = if (isCheckChanged) index else -1
                response.setSelected(latestSelectedItem)
            }

            if (index < response.getResponseItemList().size - 1) {
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
fun CheckBoxListContainer(
    responseItem: ResponseItem,
    isChecked: Boolean,
    onCheckedChange: (Boolean, ResponseItem) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = contentSpacing, vertical = contentSpacingSmall)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = contentSpacing)
        ) {
            Text(
                text = responseItem.name,
                modifier = Modifier.wrapContentSize(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = textSizeBody
                )
            )
            Text(
                text = responseItem.getRawResFileName(Util.getContext()),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = contentSpacingTiny),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = textSizeBody
                )
            )
        }
        Text(
            text = StringUtils.viewJson,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
                .clickable { showDialog.value = true }
                .underLine(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = textSizeBody
            )
        )

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                text = {
                    Text(Util.jsonFileReader(responseItem))
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog.value = false
                    }) {
                        Text(StringUtils.buttonOk)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog.value = false
                    }) {
                        Text(StringUtils.buttonCancel)
                    }
                }
            )
        }

        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it, responseItem)
            },
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ApiDetailViewPreview() {
    AndroidMockServerTheme {
        ApiDetailViewContainer(MockDataViewModel())
    }
}



