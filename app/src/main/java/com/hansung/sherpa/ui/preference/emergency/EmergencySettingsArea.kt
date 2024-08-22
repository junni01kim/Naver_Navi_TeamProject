package com.hansung.sherpa.ui.preference.emergency

import android.widget.Toast
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.dialog.SherpaDialog
import com.hansung.sherpa.emergency.Emergency
import com.hansung.sherpa.emergency.EmergencyManager
import com.hansung.sherpa.emergency.EmergencyResponse
import com.hansung.sherpa.ui.preference.Divider
import com.hansung.sherpa.ui.theme.tertiaryLight
import com.hansung.sherpa.user.UserManager

/**
 * 설정 창 긴급 연락처 화면
 * 긴급 연락처 조회 삭제 등이 가능하다.
 *
 */
@Composable
fun EmergencySettingsArea(
    finish : () -> Unit
) {
    // Dialog 확장 여부를 관리하는 변수
    var deleteDialogExpand by remember{ mutableStateOf(false) }
    var emrgInfoExpand by remember{ mutableStateOf(false) }
    var createDialogExpand by remember{ mutableStateOf(false) }

    // 리스트 선택 및 삭제 될 리스트의 Index를 알려주는 변수 ( emrgList에서 아무것도 선택되지 않았을 때는 -1)
    var clickedIndex by remember{ mutableIntStateOf(-1) }

    // (2024-08-16) 에러 처리 완료
    val emergencyListResponse = EmergencyManager().getAllEmergency(StaticValue.userInfo.userId!!)
    val userEmrgList = if(emergencyListResponse.code == 200) emergencyListResponse.data!! else listOf()

    val emrgList = remember { mutableStateListOf<Emergency>().apply { addAll(userEmrgList) }}

    // TODO: 사용자로 로그인 했을 때 한정으로 제작
    // 보호자 선택 시 사용될 변수
    val userRelation = UserManager().getRelation(StaticValue.userInfo.userId!!)
    val caregiverUser1 = UserManager().getUser(userRelation.caregiverId)

    val caregiverName = userRelation.relation // 보호자가 지정한 보호자 별명
    val caregiverAddress = caregiverUser1.userAccount?.address
    val caregiverTelNum = caregiverUser1.telNum

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        /**
         * 긴급 연락처 조회 다이얼로그
         *
         */
        /**
         * 긴급 연락처 조회 다이얼로그
         *
         */
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
        /**
         * 긴급 연락처 추가 다이얼로그
         *
         */
        /**
         * 긴급 연락처 추가 다이얼로그
         *
         */
        if(createDialogExpand){
            CreateDialog(
                onCloseRequest = { createDialogExpand = !createDialogExpand },
                createRequest = { name, telNum, address ->
                    // 긴급 연락처 추가 API
                    val newEmergency = Emergency(
                        userId = StaticValue.userInfo.userId!!,
                        name = name,
                        telNum = telNum,
                        address = address
                    )

                    // (2024-08-16) 에러 처리 완료
                    val emergencyResponse = EmergencyManager().insertEmergency(newEmergency)
                    if(emergencyResponse.code == 200){
                        val returnEmergency = emergencyResponse.data!!
                        emrgList.add(returnEmergency)
                    }
                }
            )
        }

        /**
         * 긴급 연락처 삭제 다이얼로그
         * Emergency Table의 name이 Unique하므로 name을 통해 삭제 예정
         */

        /**
         * 긴급 연락처 삭제 다이얼로그
         * Emergency Table의 name이 Unique하므로 name을 통해 삭제 예정
         */
        if(deleteDialogExpand) {
            val context = LocalContext.current
            DeleteDialogUI(
                name = emrgList[clickedIndex].name,
                onCloseRequest = { deleteDialogExpand = false },
                onDeleteRequest = {
                    // (2024-08-16) 에러 처리 완료
                    val deleteEmergencyResponse = EmergencyManager().deleteEmergency(emrgList[clickedIndex].emergencyId)
                    if(deleteEmergencyResponse.code != 200) Toast.makeText(context, "긴급 연락처 삭제 실패", Toast.LENGTH_SHORT).show()
                    else {
                        emrgList.removeAt(clickedIndex)
                        clickedIndex = -1
                        deleteDialogExpand = false
                    }
                }
            )
        }

        /**
         * 보호자와 긴급 연락처의 리스트를 보여 주는 영역
         *
         */

        /**
         * 보호자와 긴급 연락처의 리스트를 보여 주는 영역
         *
         */
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // 보호자 연락처에 관한 정보가 작성된 영역
            item { Divider("보호자 연락처") }
            item {
                ItemRow(
                    name = caregiverName,
                    address = caregiverAddress,
                    telNum = caregiverTelNum,
                    deleteItem = {/* 삭제기능 이용 X */},
                    showItem = {
                        emrgInfoExpand = !emrgInfoExpand
                    }
                )
            }

            /**
             * 긴급 연락처에 관한 정보가 작성된 영역
             *
             */

            /**
             * 긴급 연락처에 관한 정보가 작성된 영역
             *
             */

            /**
             * 긴급 연락처에 관한 정보가 작성된 영역
             *
             */

            /**
             * 긴급 연락처에 관한 정보가 작성된 영역
             *
             */
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
                    showItem = {
                        clickedIndex = index
                        emrgInfoExpand = !emrgInfoExpand
                    }
                )
            }

            /**
             * 긴급 연락처 추가 버튼이 작성된 영역
             *
             */

            /**
             * 긴급 연락처 추가 버튼이 작성된 영역
             *
             */

            /**
             * 긴급 연락처 추가 버튼이 작성된 영역
             *
             */

            /**
             * 긴급 연락처 추가 버튼이 작성된 영역
             *
             */
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
