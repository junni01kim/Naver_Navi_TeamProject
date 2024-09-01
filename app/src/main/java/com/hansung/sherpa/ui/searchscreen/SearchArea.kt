package com.hansung.sherpa.ui.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.LatLng
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.navigation.Navigation

val searchScreenUiColor = Color.White
val iconColor = Color(0xFF82878B)
val textColor = Color(0xFF424242)
val placeholderColor = Color(0xFFAAAAAA)

/**
 * 검색과 관련된 Composable을 작성하는 공간
 * 출발지와 도착지를 입력하고, 입력 값을 기반으로 경로를 요청하는 영역
 *
 * -구성-
 * 버튼: 검색, 뒤로가기, 변경 버튼
 * 입력창: 출발지, 목적지
 * 
 * SortingArea: 결과 경로 리스트를 정렬하여 보여주기 설정 영역
 * LocationList: 검색어를 이용해 검색 위치를 결정하기 위한 영역
 *
 * @param navController 홈화면 navController 원형 ※ 해당 Composable에서는 뒤로가기를 구현하기 위해 참조
 * @param _destinationValue HomeScreen에서 작성한 목적지 정보를 가져오기 위한 String값
 * @param update State Hoisting을 이용하여 SearchScreen의 다른 요소에 값을 전달해주기 위한 함수
 *
 * ※ Preview는 SearchScreen에서 실행할 것
 */
@Composable
fun SearchArea(navController: NavController, _destinationValue: String, dialogToggle: MutableState<Boolean>, update: (List<TransportRoute>, Long) -> Unit) {
    // ===== 저장되는 데이터 목록 =====
    // Departure TextField, Destination TextField에 사용할 변수: 문자열(String)
    var departureValue by remember { mutableStateOf("") }
    var destinationValue by remember { mutableStateOf("") }

    // Departure TextField, Destination TextField에 사용할 변수: 좌표값(LatLng)
    var departureLatLng by remember {mutableStateOf(LatLng(-1.0,-1.0))}
    var destinationLatLng by remember {mutableStateOf(LatLng(-1.0,-1.0))}

    // 검색 키워드를 저장하는 변수 // HomeScreen에서 받아온 값이 기존에 들어온다.
    var locationValue by remember { mutableStateOf(if (_destinationValue=="아무것도 전달되지 않았음") "" else _destinationValue) }

    // 검색을 한 시간 정보를 담는 변수
    var searchingTime by remember { mutableStateOf(0L) }

    // Search Button을 눌렀는데, 값이 저장되어 있지 않을 때 해당 TextField로 포커스를 옮기기 위한 객체
    val departureFocusRequester = FocusRequester()
    val destinationFocusRequester = FocusRequester()

    // locationValue에 저장된 정보가 어느 TextField의 키워드인지 구분하는 Flag (false면 출발지 true이면 목적지)
    var type by remember{mutableStateOf(true)}

    // 아이템 간격 모듈화: dp
    val rowMargin = 20.dp
    val bigSpace = 10.dp
    val middleSpace = 5.dp
    val smallSpace = 2.dp
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(searchScreenUiColor)
                .padding(vertical = rowMargin)
        ) {
            /**
             * Change Button
             *
             * Departure TextField/ Destination TextField 텍스트 변경 버튼
             */
            SherpaButton(ImageVector.vectorResource(R.drawable.swap)){
                val tempString = departureValue
                val tempLatLng = departureLatLng
                departureValue = destinationValue
                departureLatLng = destinationLatLng
                destinationValue = tempString
                destinationLatLng = tempLatLng
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    /**
                     * Departure TextField
                     *
                     * 출발지 입력창
                     */
                    SherpaTextField(
                        value = departureValue,
                        onValueChange = {departureValue = it},
                        placeholderText = "출발지를 입력하세요",
                        focusRequester = departureFocusRequester,
                        onDone = {
                            locationValue = departureValue
                            type = false

                            departureFocusRequester.freeFocus()
                        })
                    Spacer(modifier = Modifier.width(middleSpace))
                    /**
                     * Back Button
                     *
                     * 뒤로가기(홈화면) 이동 창
                     */
                    SherpaButton(Icons.Default.Close) { navController.popBackStack() }
                }

                Spacer(modifier = Modifier.height(bigSpace))

                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    /**
                     * Destination TextField
                     *
                     * 도착지 입력창
                     */
                    SherpaTextField(
                        value = destinationValue,
                        onValueChange = { destinationValue = it },
                        placeholderText = "목적지를 입력하세요",
                        focusRequester = destinationFocusRequester,
                        onDone = {
                            locationValue = destinationValue
                            type = true
                        }
                    )
                    Spacer(modifier = Modifier.width(middleSpace))
                    /**
                     * Search Button
                     *
                     * 경로 검색 버튼
                     */
                    SherpaButton(Icons.Default.Search) {
                        /**
                         * 각 TextField의 장소가 LocationList를 통해 저장되었는지 확인하는 함수
                         */
                        if(departureLatLng == LatLng(-1.0,-1.0) || departureValue == ""){
                            departureLatLng == LatLng(-1.0,-1.0)
                            departureValue == ""
                            departureFocusRequester.requestFocus()
                            return@SherpaButton
                        }
                        if(destinationLatLng == LatLng(-1.0,-1.0) || destinationValue == ""){
                            destinationLatLng == LatLng(-1.0,-1.0)
                            destinationValue == ""
                            destinationFocusRequester.requestFocus()
                            return@SherpaButton
                        }

//                            if(StaticValue.userInfo.role1 == "CARETAKER" && !StaticValue.searchPermission) {
//                                dialogToggle.value = true
//                                return@SherpaButton
//                            }

                        /**
                         * 출발지와 목적지에 대한 경로를 요청하는 함수
                         *
                         * 동일명 LatLng 클래스를 중복하여 사용하여 코드가 길어졌다.
                         * ※ 네이버 LatLng이 필요하나, 네이버 LatLng 클래스에는 getLatLng()함수가 없어 중복하여 사용하였다.
                         */
                        val transportRoutes =
                            Navigation().getDetailTransitRoutes(
                                com.naver.maps.geometry.LatLng(departureLatLng.latitude,departureLatLng.longitude),
                                com.naver.maps.geometry.LatLng(destinationLatLng.latitude,destinationLatLng.longitude),
                                departureValue, destinationValue)

                        /**
                         * 검색시간을 요청하는 함수.
                         *
                         * SortingArea와 ExpandableCard에서 이용한다.
                         */
                        searchingTime = System.currentTimeMillis()
                        update(transportRoutes, searchingTime)

                        /**
                         * TextField 값 초기화
                         */
                        departureValue = ""
                        departureLatLng = LatLng(-1.0,-1.0)
                        destinationValue = ""
                        destinationLatLng = LatLng(-1.0,-1.0)
                        StaticValue.searchPermission = false
                    }
                }
            }
        }
    }

    Box {
        /**
         * 하단 LazyColumn item을 정렬 방식을 지정하는 Composable
         *
         * 결과 경로 리스트를 정렬하여 보여주기 설정 영역
         * ※ 2024-08-06 미완
         */
        SortingArea(searchingTime)

        /**
         * 검색 키워드를 통해 장소를 선택하기 위한 영역
         * 최종적으로 장소(출발지 or 목적지)를 선택하는 영역이다.
         */
        LocationList(locationValue) { childLocationValue, childLocationLatLng ->
            if(type == false){
                departureValue = childLocationValue
                departureLatLng = childLocationLatLng
            }
            else {
                destinationValue = childLocationValue
                destinationLatLng = childLocationLatLng
            }
        }
    }
}

@Composable
fun SherpaTextField(value:String, onValueChange: (String) -> Unit, placeholderText: String, focusRequester: FocusRequester, onDone: KeyboardActionScope.() -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(51.dp)
            .focusRequester(focusRequester)
            .shadow(10.dp, shape = MaterialTheme.shapes.small),
        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = searchScreenUiColor,
            unfocusedContainerColor = searchScreenUiColor,

            focusedPlaceholderColor = placeholderColor,
            unfocusedPlaceholderColor = placeholderColor,

            focusedTextColor = textColor,
            unfocusedTextColor = textColor,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        placeholder = { Text(placeholderText, fontSize = 13.sp) },
        keyboardActions = KeyboardActions(onDone = {
            onDone()
            keyboardController?.hide()
        })
    )
}

@Composable
fun SherpaIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        modifier = Modifier.size(30.dp),
        tint = iconColor,
        contentDescription = null
    )
}

@Composable
fun SherpaButton(icon:ImageVector, onClick: () -> Unit){
    IconButton(modifier = Modifier.size(60.dp, 50.dp),
        onClick = { onClick() }) {
        // 버튼에 들어갈 이미지
        SherpaIcon(icon)
    }
}

@Preview
@Composable
fun SearchAreaPreview(){
    SearchScreen()
}
