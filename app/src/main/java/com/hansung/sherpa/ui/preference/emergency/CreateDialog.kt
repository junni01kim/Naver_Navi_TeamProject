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
import androidx.compose.material3.TextButton
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
import com.hansung.sherpa.ui.preference.calendar.LocationBottomSheet
import com.hansung.sherpa.ui.theme.PurpleGrey40

/**
 * 긴급 연락처 추가에 이용할 다이얼로그
 *
 * @param onCloseRequest 화면을 닫을 때 이용하는 람다 함수. 상태 Hoisting에 이용한다.
 * @param createRequest 긴급 연락처 추가에 이용할 람다 함수. ※ API를 이용할 때 필요없어 질 함수이다.
 */
@Composable
fun CreateDialog(
    onCloseRequest: () -> Unit,
    createRequest: (Emergency) -> Unit
) {
    // TextField 작성 시 이용할 변수
    var name by remember { mutableStateOf("") }
    var telNum by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // 지역 검색 API에 이용할 하단 시트 UI (김상준 팀원 코드 이용)
    val locationSheetState = remember { mutableStateOf(false) }

    // 필수 항목을 작성하지 않고, 추가 버튼을 누를 시 포커스를 옮기기 위한 변수 
    val nameFocusRequester = remember { FocusRequester() }
    val telNumFocusRequester = remember { FocusRequester() }
    
    // 다이얼로그 작성 시 주변화면을 어둡게하고, 터치가 불가능하게 하기 위한 코드
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .zIndex(1f)
            .clickable(enabled = false, onClick = {})
    )

    // 긴급 연락처 추가 Dialog (김상준 팀원 코드 인용)
    val lightGrayColor = Color(229,226,234)
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
                    /**
                     * 제목(title)
                     *
                     */
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

                    /**
                     * 내용(body)
                     *
                     * 이름, 연락처를 제외한 내용은 User1 정보와 서버DB에서 가져온다.
                     */
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
                    // -----------------------

                    Spacer(modifier = Modifier.padding(4.dp))
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(lightGrayColor),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // 긴급 연락처 추가 취소 버튼
                    TextButton(
                        onClick = { onCloseRequest() }
                    ) {
                        Text(
                            "취소",
                            fontWeight = FontWeight.Bold,
                            color = PurpleGrey40,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }

                    // 긴급 연락처 추가 버튼
                    TextButton(
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
                            createRequest(Emergency(100, 1, "y", name, telNum, address))

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

        // 긴급 연락처의 주소를 지정하기 위한 영역
        if(locationSheetState.value) {
            LocationBottomSheet(locationSheetState) {
                address = it.address.toString()
            }
        }
    }
}

/**
 * 항목 추가 및 디자인 모듈화
 *
 * @param title 제목
 * @param text textField에 작성되는 내용
 * @param focusRequester textField focus
 * @param onValueChange textField onValueChange
 */
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
            // BasicTextField만 쓰니까 허전한거 같아서, TextField 영역에 밑줄 추가.
            Box(modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(1.dp))
        }
    }
}

@Preview
@Composable
fun CreateDialogPreview() {
    EmergencySettingsScreen()
}