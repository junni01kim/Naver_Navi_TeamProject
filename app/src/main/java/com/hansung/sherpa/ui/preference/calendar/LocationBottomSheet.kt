package com.hansung.sherpa.ui.preference.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.searchlocation.Items
import com.hansung.sherpa.searchlocation.SearchLoactionCallBack
import com.hansung.sherpa.searchlocation.SearchLocation
import com.hansung.sherpa.searchlocation.SearchLocationResponse


/**
 * 위치 검색하는 ModalBottomSheet
 *
 * @param locationSheetStatus 해당 bottomsheet 열고 닫는 상태 변수
 * @param onSelectedLocation 선택 되었을 때 해당 아이템 callback
 */
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
            .heightIn(min = 450.dp),
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

/**
 * 검색창 부분
 *
 * @param text 검색창에 입력한 문자열 다른 함수에서도 사용 해야 함
 * @param onSearch 검색한 경우 상태가 바뀜
 *
 */
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

/**
 * 검색 시 위치가 표시 되는 부분
 *
 * @param text 사용자가 입력한 위치 문자열
 * @param onSearch 사용자가 검색한 경우 상태가 바뀜
 * @param locationSheetStatus 해당 bottomsheet 열고 닫는 상태 변수
 * @param onSelectedLocation 선택 되었을 때 해당 아이템 callback
 */

@Composable
fun LocationList(
    text: MutableState<String>,
    onSearch: MutableState<Boolean>,
    locationSheetStatus : MutableState<Boolean>,
    onSelectedLocation : (Items) -> Unit
) {
    val locationItems = remember { mutableStateListOf<Pair<String?,String?>>() }
    val isValidItems = remember { mutableStateListOf<Boolean>() }
    val itemList = remember { mutableStateOf(ArrayList<Items>()) }

    /**
     * 검색 되었을 때만 표시함
     */
    LaunchedEffect(onSearch.value) {
        if (onSearch.value && text.value.isNotEmpty()) {
            locationItems.clear()
            isValidItems.clear()
            SearchLocation().search(text.value, object : SearchLoactionCallBack{
                override fun onSuccess(response: SearchLocationResponse?) {
                    response?.items?.forEachIndexed { index, item ->
                        if (index < 6) {
                            locationItems.add(Pair(item.title, item.roadAddress))
                            isValidItems.add(true)
                        }
                    }
                    if (locationItems.isEmpty()) {
                        locationItems.add(Pair("검색 결과가 없습니다.",null))
                        isValidItems.add(true)
                    }
                    itemList.value = response?.items ?: arrayListOf()
                    onSearch.value = false
                }
                override fun onFailure(message: String) {
                    locationItems.add(Pair("검색 결과가 없습니다.",null))
                    isValidItems.add(true)
                    onSearch.value = false
                }
            })
        }
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        LazyColumn(modifier = Modifier.heightIn(max = 700.dp)) {
            // list의 사이즈만큼 출력
            items(locationItems.size) { index ->
                LocationColumn(
                    index = index,
                    locationText = remember { mutableStateOf(locationItems[index]) },
                    isValid = remember { mutableStateOf(isValidItems[index]) }
                    // 아이템을 선택한 경우 수행
                ){ selectedIndex ->
                    text.value = itemList.value[selectedIndex].title.toString()
                    locationSheetStatus.value = false
                    onSelectedLocation(itemList.value[selectedIndex])
                }
            }
        }
    }
}

/**
 * 화면에 표시되는 각각의 위치
 *
 * @param index 몇 번째 아이템 인지
 * @param locationText Pair<위치 이름?, 주소?>
 * @param isValid 유효한 아이템 인지
 * @param selectLocation 선택된 아이템의 index 번호 callback
 */
@Composable
fun LocationColumn(
    index : Int,
    locationText : MutableState<Pair<String?,String?>>,
    isValid : MutableState<Boolean>,
    selectLocation : (Int) -> Unit
){
    Column(
        modifier = Modifier.let {
            when {
                isValid.value -> it.fillMaxWidth()
                else -> it.size(0.dp)
            }
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clickable {
                    selectLocation(index)
                }

        }
    ){
        Text(
            text = locationText.value.first.toString(),
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.DarkGray
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if(locationText.value.second != null){
            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = locationText.value.second.toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
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