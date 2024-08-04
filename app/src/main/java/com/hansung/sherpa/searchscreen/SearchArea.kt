package com.hansung.sherpa.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.R
import com.hansung.sherpa.compose.navigation.Navigation
import com.hansung.sherpa.itemsetting.TransportRoute

/**
 * 검색과 관련된 Composable을 작성하는 공간
 * 출발지와 도착지를 입력하고, 입력 값을 기반으로 경로를 요청하는 영역
 *
 * -구성-
 * 버튼: 검색, 뒤로가기, 변경 버튼
 * 입력창: 출발지, 목적지
 *
 * @param navController 홈화면 navController 원형 ※ 해당 Composable에서는 뒤로가기를 구현하기 위해 참조
 * @param _destinationValue HomeScreen에서 작성한 목적지 정보를 가져오기 위한 String값
 * @param update State Hoisting을 이용하여 SearchScreen의 다른 요소에 값을 전달해주기 위한 함수
 *
 * ※ Preview는 SearchScreen에서 실행할 것
 */
@Composable
fun SearchArea(navController: NavController, _destinationValue: String, update: (List<TransportRoute>, Long) -> Unit) {
    // 저장되는 데이터 목록
    // Departure TextField, Destination TextField에 사용할 변수
    var departureValue by remember { mutableStateOf("") }
    var destinationValue by remember { mutableStateOf(if (_destinationValue=="아무것도 전달되지 않았음") "" else _destinationValue) }

    // 아이템 간격 모듈화
    val space = 10.dp
    Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .padding(vertical = 5.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center){
            /**
             * Change Button
             *
             * Departure TextField/ Destination TextField 텍스트 변경 버튼
             */
            IconButton(modifier = Property.Button.modifier,
                onClick = {
                    val tempString = departureValue
                    departureValue = destinationValue
                    destinationValue = tempString
                }) {
                // 버튼에 들어갈 이미지
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.swap),
                    modifier = Property.Icon.modifier,
                    tint = Property.Icon.tint,
                    contentDescription = "텍스트 전환 버튼 아이콘"
                )
            }
        }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically){
                /**
                 * Departure TextField
                 *
                 * 출발지 입력창
                 */
                TextField(
                    value = departureValue,
                    onValueChange = {departureValue = it},
                    modifier = Property.TextField.modifier,
                    textStyle = Property.TextField.textStyle,
                    shape = Property.TextField.shape,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Property.TextField.containerColor, unfocusedTextColor = Property.TextField.textColor, focusedContainerColor = Property.TextField.containerColor),
                    singleLine = Property.TextField.singleLine,
                    placeholder = { Text("출발지를 입력하세요", fontSize = 12.sp) }
                )
                Spacer(modifier = Modifier.width(5.dp))
                /**
                 * Back Button
                 *
                 * 뒤로가기(홈화면) 이동 창
                 */
                IconButton(modifier = Property.Button.modifier,
                    onClick = {
                        navController.popBackStack()
                    }) {
                    // 버튼에 들어갈 이미지
                    Icon(
                        imageVector = Icons.Default.Close,
                        modifier = Property.Icon.modifier,
                        tint = Property.Icon.tint,
                        contentDescription = "뒤로가기 버튼 아이콘"
                    )
                }
            }

            Spacer(modifier = Modifier.height(space))

            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                /**
                 * Destination TextField
                 *
                 * 도착지 입력창
                 */
                TextField(
                    value = destinationValue,
                    onValueChange = {destinationValue = it},
                    modifier = Property.TextField.modifier,
                    textStyle = Property.TextField.textStyle,
                    shape = Property.TextField.shape,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Property.TextField.containerColor, unfocusedTextColor = Property.TextField.textColor, focusedContainerColor = Property.TextField.containerColor),
                    singleLine = Property.TextField.singleLine,
                    placeholder = { Text("목적지를 입력하세요", fontSize = 12.sp) }
                )
                Spacer(modifier = Modifier.width(5.dp))

                /**
                 * Search Button
                 *
                 * 경로 검색하기 버튼
                 * 1) TextField에 있던 값들을 지우고
                 * 2) 해당 내용을 기반으로 경로를 검색한다.
                 */
                IconButton(modifier = Property.Button.modifier,
                    onClick = {
                        departureValue = ""
                        destinationValue = ""

                        // 테스트용 코드 (하단에 코드 샘플 기재) << 부끄러우니까 보지 마세요
                        val transportRoutes = Navigation().getDetailTransitRoutes("tempString","tempString")
                        update(transportRoutes, System.currentTimeMillis())
                    }) {
                    // 버튼에 들어갈 이미지
                    Icon(
                        imageVector = Icons.Default.Search,
                        modifier = Property.Icon.modifier,
                        tint = Property.Icon.tint,
                        contentDescription = "검색 버튼 아이콘"
                    )
                }
            }
        }
    }
}