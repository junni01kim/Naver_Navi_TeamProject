package com.hansung.sherpa.ui.preference.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.dialog.SherpaDialog
import com.hansung.sherpa.ui.preference.Divider

/**
 * 설정창 긴급 연락처 화면
 * 긴급 연락처 조회 삭제 등이 가능하다.
 *
 */
@Composable
fun EmergencySettingsScreen() {
    // Dialog 확장 여부를 관리하는 변수
    var deleteDialogExpand by remember{mutableStateOf(false)}
    var emrgInfoExpand by remember{mutableStateOf(false)}
    var createDialogExpand by remember{mutableStateOf(false)}

    // ------------------------------ 샘플 데이터 ------------------------------------
    // 리스트 선택 및 삭제 될 리스트의 Index를 알려주는 변수 ( emrgList에서 아무것도 선택되지 않았을 때는 -1)
    var clickedIndex by remember{mutableStateOf(-1)}

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
    var emrgList = remember { mutableStateListOf(
        Emergency(11, 1, "y", "병원", "02-760-0000", "성북구 삼선교로"),
        Emergency(12, 1, "y", "김상준", "010-9570-1111", "용산"),
        Emergency(13, 1, "y", "이준희", "010-2916-2222", "홍대"))
    }
    // ------------------------------------------------------------------

    // 보호자 선택 시 사용될 변수
    val caregiverName = caregiverUserRelation.relation.toString()
    val caregiverAddress = caregiverUserAccount.address.toString()
    val caregiverTelNum = caregiverUser1.telNum.toString()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 긴급 연락처 조회 다이얼로그
        if (emrgInfoExpand) {
            // clickedIndex가 -1이라는 뜻은 보호자 연락처가 클릭되었다는 뜻이다.
            SherpaDialog(
                title = if(clickedIndex == -1) caregiverName else emrgList[clickedIndex].name,
                message = listOf(
                    "주소: ${if(clickedIndex == -1) caregiverAddress else emrgList[clickedIndex].address}",
                    "연락처: ${if(clickedIndex == -1) caregiverTelNum else emrgList[clickedIndex].telNum}"),
                confirmButtonText = "확인",
                onDismissRequest = {
                    emrgInfoExpand = false
                    clickedIndex = -1
                                   },
                onConfirmation = {
                    emrgInfoExpand = false
                    clickedIndex = -1
                }
            )
        }
        // 긴급 연락처 추가 다이얼로그
        if(createDialogExpand){
            CreateDialog(
                onCloseRequest = { createDialogExpand = !createDialogExpand },
                updateRequest = {
                    // TODO: 1. 긴급 연락처 추가 API 연결
                    emrgList.add(it) // 임시 추가 로직 삭제처리 할 것
                    // TODO: 2. 긴급 연락처 정보 불러오기
                }
            )
        }

        // 긴급 연락처 삭제 다이얼로그
        if(deleteDialogExpand) {
            DeleteDialogUI(
                name = emrgList[clickedIndex].name,
                address = emrgList[clickedIndex].address,
                telNum = emrgList[clickedIndex].telNum,
                onDismissRequest = { deleteDialogExpand = false },
                onDeleteRequest = {
                    // TODO: 1. 긴급 연락처 삭제 API 연결
                    // TODO: 2. 긴급 연락처 정보 불러오기
                    emrgList.removeAt(clickedIndex)
                    clickedIndex = -1
                    deleteDialogExpand = false
                }
            )
        }

        // 보호자와 긴급 연락처의 리스트를 보여 주는 영역
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // 보호자 연락처에 관한 정보가 작성된 영역
            item { Divider("보호자 연락처") }
            item {
                ItemRow(
                    name = caregiverUserRelation.relation,
                    address = caregiverUserAccount.address,
                    telNum = caregiverUser1.telNum,
                    deleteItem = {/* 삭제기능 이용 X */},
                    openItemInfo = {
                        emrgInfoExpand = !emrgInfoExpand
                    }
                )
            }

            // 긴급 연락처에 관한 정보가 작성된 영역
            item{ Divider("긴급 연락처") }
            // Emergency 클래스의 이름(name) 주소(address) 전화번호(telNum)가 나타난다.
            itemsIndexed(emrgList) { index, it ->
                ItemRow(
                    name = it.name,
                    address = it.address,
                    telNum = it.telNum,
                    deleteItem = {
                        clickedIndex = index
                        deleteDialogExpand = !deleteDialogExpand
                    },
                    openItemInfo = {
                        clickedIndex = index
                        emrgInfoExpand = !emrgInfoExpand
                    }
                )
            }

            // 긴급 연락처 추가 버튼이 작성된 영역
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 5.dp)
                        .clickable(onClick = {
                            createDialogExpand = true
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