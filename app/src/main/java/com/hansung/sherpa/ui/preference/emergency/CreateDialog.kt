package com.hansung.sherpa.ui.preference.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.hansung.sherpa.ui.preference.LocationBottomSheet
import com.hansung.sherpa.ui.theme.PurpleGrey40

@Composable
fun CreateDialog(
    onCloseRequest: () -> Unit,
    updateRequest: (Emergency) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var telNum by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val locationSheetState = remember { mutableStateOf(false) }

    val nameFocusRequester = remember { FocusRequester() }
    val telNumFocusRequester = remember { FocusRequester() }

    val lightGrayColor = Color(229,226,234)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .zIndex(1f)
            .clickable(enabled = false, onClick = {})
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(horizontal = 50.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
            Column(
                Modifier.background(Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "긴급 연락처 추가",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    InputTextField("이름", name, nameFocusRequester){ name = it }
                    InputTextField("연락처", telNum, telNumFocusRequester){ telNum = it }

                    // ----- address 관련 (InputTextField 변형)-----
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "주소",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(60.dp)
                        )
                        Column {
                            Text(
                                text = address,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { locationSheetState.value = true },
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Box(
                                modifier = Modifier
                                    .background(Color.LightGray)
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                        }
                    }
                    // ----- ----------- -----

                    Spacer(modifier = Modifier.padding(4.dp))
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(lightGrayColor),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    androidx.compose.material3.TextButton(
                        onClick = { onCloseRequest() }
                    ) {
                        Text(
                            "취소",
                            fontWeight = FontWeight.Bold,
                            color = PurpleGrey40,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                    androidx.compose.material3.TextButton(
                        onClick = {
                            // 모든 값이 설정되었는지 확인 (※ 우선 주소는 필수 아님 설정) TODO: 서식 확인 예외처리
                            if(name == "") {
                                nameFocusRequester.requestFocus()
                                return@TextButton
                            }
                            if(telNum == "") {
                                nameFocusRequester.requestFocus()
                                return@TextButton
                            }

                            // TODO: 긴급 연락처 추가 API

                            // TODO: DB 내용 불러오는 함수지만 일단 임시 정보 가져오는 것으로 작업
                            // TODO: DB 내용 불러오는 이유는 안정성을 위함
                            updateRequest(Emergency(100, 1, "y", name, telNum, address))

                            onCloseRequest()
                        }
                    ) {
                        Text(
                            "추가",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(219, 0, 35),
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
            }
        }

        if(locationSheetState.value) {
            LocationBottomSheet(locationSheetState) {
                address = it.address.toString()
            }
        }
    }
}

@Preview
@Composable
fun CreateDialogPreview() {
    EmergencySettingsScreen()
}

@Composable
fun InputTextField(title:String, text:String, focusRequester: FocusRequester, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(60.dp),
        )
        Column{
            BasicTextField(
                value = text,
                onValueChange = { onValueChange(it) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = TextStyle.Default.copy(
                    textAlign = TextAlign.Center
                )
            )
            Box(modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(1.dp))
        }
    }
}