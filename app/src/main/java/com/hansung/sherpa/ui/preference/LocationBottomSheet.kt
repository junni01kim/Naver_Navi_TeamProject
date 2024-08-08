package com.hansung.sherpa.ui.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.searchlocation.Items
import com.hansung.sherpa.searchlocation.SearchLoactionCallBack
import com.hansung.sherpa.searchlocation.SearchLocation
import com.hansung.sherpa.searchlocation.SearchLocationResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(
    locationSheetStatus : MutableState<Boolean>,
    onSelectedLocation: (Items) -> Unit
){
    val onSearch = remember { mutableStateOf(false) }
    val text = remember { mutableStateOf("") }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = { locationSheetStatus.value = false },
        modifier = Modifier
            .fillMaxHeight(0.6f)
            .heightIn(min = 400.dp),
    ){
        LazyColumn {
            item {
                LocationTextField(text = text, onSearch = onSearch)
                LocationList(text = text, onSearch = onSearch, locationSheetStatus = locationSheetStatus){
                    onSelectedLocation(it)
                }
            }
        }
    }
}

@Composable
fun LocationTextField(
    text : MutableState<String>,
    onSearch : MutableState<Boolean>
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val lightGrayColor = Color(229,226,234)
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ){
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(lightGrayColor)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.padding(start = 12.dp))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .offset(y = 1.dp)
            )
            TextField(
                value = text.value,
                onValueChange = { newText ->
                    if (!newText.contains('\t') && !newText.contains('\n')) {
                        text.value = newText
                    }
                },
                placeholder = { Text(text = "위치")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = lightGrayColor,
                    focusedContainerColor = lightGrayColor,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Characters,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    showKeyboardOnFocus = null ?: true
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    onSearch.value = true
                }),
            )

        }
    }
}

@Composable
fun LocationList(
    text: MutableState<String>,
    onSearch: MutableState<Boolean>,
    locationSheetStatus : MutableState<Boolean>,
    onSelectedLocation : (Items) -> Unit
) {
    val locationItems = remember { mutableStateListOf<String>() }
    val isValidItems = remember { mutableStateListOf<Boolean>() }
    val itemList = remember { mutableStateOf(ArrayList<Items>()) }

    LaunchedEffect(onSearch.value) {
        if (onSearch.value && text.value.isNotEmpty()) {
            locationItems.clear()
            isValidItems.clear()
            SearchLocation().search(text.value, object : SearchLoactionCallBack{
                override fun onSuccess(response: SearchLocationResponse?) {
                    response?.items?.forEachIndexed { index, item ->
                        if (index < 6) {
                            locationItems.add(item.title.toString())
                            isValidItems.add(true)
                        }
                    }
                    if (locationItems.isEmpty()) {
                        locationItems.add("검색 결과가 없습니다.")
                        isValidItems.add(true)
                    }
                    itemList.value = response?.items ?: arrayListOf()
                    onSearch.value = false
                }
                override fun onFailure(message: String) {
                    locationItems.add("검색 결과가 없습니다.")
                    isValidItems.add(true)
                    onSearch.value = false
                }
            })
        }
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        LazyColumn(modifier = Modifier.heightIn(max = 700.dp)) {
            items(locationItems.size) { index ->
                LocationColumn(
                    index = index,
                    locationText = remember { mutableStateOf(locationItems[index]) },
                    isValid = remember { mutableStateOf(isValidItems[index]) }
                ){ selectedIndex ->
                    text.value = itemList.value[selectedIndex].title.toString()
                    locationSheetStatus.value = false
                    onSelectedLocation(itemList.value[selectedIndex])
                }
            }
        }
    }
}

@Composable
fun LocationColumn(
    index : Int,
    locationText : MutableState<String>,
    isValid : MutableState<Boolean>,
    selectLocation : (Int) -> Unit
){
    Column(
        modifier = Modifier.let {
            when {
                isValid.value -> it.fillMaxWidth()
                else -> it.size(0.dp)
            }
                .padding(10.dp)
                .clickable {
                    selectLocation(index)
                }
        }
    ){
        Text(text = locationText.value)
    }
}

@Preview
@Composable
fun Preview(){
    val text = remember { mutableStateOf("") }
    val onSearch = remember {
        mutableStateOf(false)
    }
    val locationSheetStatus = remember {
        mutableStateOf(false)
    }
    val onSelectedLocation : (Items) -> Unit = {}
    LazyColumn {
        item {
            LocationTextField(text = text, onSearch = onSearch)
            LocationList(text = text, onSearch = onSearch, locationSheetStatus = locationSheetStatus){
                onSelectedLocation(it)
            }
        }
    }
}