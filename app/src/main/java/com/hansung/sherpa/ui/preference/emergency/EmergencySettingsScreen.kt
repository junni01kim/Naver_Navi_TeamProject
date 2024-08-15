package com.hansung.sherpa.ui.preference.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.dialog.SherpaDialog
import com.hansung.sherpa.ui.preference.Divider
import com.hansung.sherpa.ui.theme.PurpleGrey40

@Composable
fun EmergencySettingsScreen() {
    var deleteDialogExpand by remember{mutableStateOf(false)}
    var emrgInfoExpand by remember{mutableStateOf(false)}

    var emrgName by remember{mutableStateOf("")}
    var emrgAddress by remember{mutableStateOf("")}
    var emrgTelNum by remember{mutableStateOf("")}

    // TODO: 서버에서 보호자 연락처 리스트 가져오기 ※ getUser1으로 가져오면 나머지(setting, account) 다 가져와 진다.
    val caregiverUserAccount = UserAccount(
        1,
        "audwnss@naver.com",
        "qweasdmnb123",
        "qweasdmnb123",
        "hashAlgorithm",
        "2001-06-15",
        "2024-08-15",
        "인천광역시 서창남로17",
        "1109동 1906호"
    )
    val caregiverUserSetting = UserSetting(1, true, true, true, false, false)
    val caregiverUser1 =
        User1(1, "김명준", "010-9032-0000", "명준", caregiverUserSetting, caregiverUserAccount)
    val caregiverUserRelation = UserRelation(11, 1, 2, "보호자")

    // TODO: 서버에서 긴급 연락처 리스트 가져오기
    var emrgList by remember{
        mutableStateOf(
            mutableListOf(
                Emergency(11, 1, "y", "병원", "02-760-0000", "성북구 삼선교로"),
                Emergency(12, 1, "y", "김상준", "010-9570-5249", "용산"),
                Emergency(13, 1, "y", "이준희", "010-2916-2027", "홍대")
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (emrgInfoExpand) {
            SherpaDialog(
                title = emrgName,
                message = listOf("주소: ${emrgAddress}", "연락처: ${emrgTelNum}"),
                confirmButtonText = "확인",
                onDismissRequest = { emrgInfoExpand = false },
                onConfirmation = { emrgInfoExpand = false }
            )
        }
        if(deleteDialogExpand) {
            CancelDialogUI(
                name = emrgName,
                address = emrgAddress,
                telNum = emrgTelNum,
                onDismissRequest = { deleteDialogExpand = false },
                onDelete = {
                    /**
                     *  TODO: 1. 삭제 API 연결
                     *  TODO: 2. 긴급 연락처 정보 불러오기
                     */
                    /**
                     *  TODO: 1. 삭제 API 연결
                     *  TODO: 2. 긴급 연락처 정보 불러오기
                     */
                    emrgList.removeAt(1)
                    deleteDialogExpand = false
                }
            )
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item { Divider("보호자 연락처") }
            item {
                ItemRow(
                    name = caregiverUserRelation.relation,
                    address = caregiverUserAccount.address,
                    telNum = caregiverUser1.telNum,
                    deleteItem = {}, // 보호자 연락처는 삭제기능 이용 안함
                    openItemInfo = {
                        emrgName = caregiverUserRelation.relation.toString()
                        emrgAddress = caregiverUserAccount.address.toString()
                        emrgTelNum = caregiverUser1.telNum.toString()
                        emrgInfoExpand = !emrgInfoExpand
                    }
                )
            }
            item{ Divider("긴급 연락처") }

            items(emrgList) {
                ItemRow(
                    name = it.name,
                    address = it.address,
                    telNum = it.telNum,
                    deleteItem = {
                        emrgName = it.name
                        emrgAddress = it.address
                        emrgTelNum = it.telNum
                        deleteDialogExpand = !deleteDialogExpand
                    },
                    openItemInfo = {
                        emrgName = it.name
                        emrgAddress = it.address
                        emrgTelNum = it.telNum
                        emrgInfoExpand = !emrgInfoExpand
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 5.dp)
                        .clickable(onClick = {
                            // TODO: 1. 긴급 연락처 정보 작성 모달 띄우기
                            // TODO: 2. 모달에서 긴급 연락처 정보 추가 API 연결하기
                            // TODO: 3. 긴급 연락처 정보 불러오기
                        }),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text("추가하기")
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun EmergencySettingsPreview() {
    EmergencySettingsScreen()
}