package com.hansung.sherpa.ui.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo.*



/**
 * nnc_backend를 기반으로 만든 긴급 연락처 클래스
 *
 * @param emergency_id
 * @param userId
 * @param deleteYn
 * @param name
 * @param telNum
 * @param address
 */
data class Emergency (
    val emergencyId: Int,
    val userId: Int,
    val deleteYn: String,
    val name: String,
    val telNum: String,
    val address: String
)

data class User1 (
    val userId:Int? = null,
    val name:String? = null,
    val telNum:String? = null,
    val role1:String? = null,
    val userSetting:UserSetting? = null,
    val userAccount:UserAccount? = null,
)

data class UserSetting (
    val userSettingId: Int? = null,
    val voiceGuide: Boolean? = null,
    val alertRoute: Boolean? = null,
    val alertDanger: Boolean? = null,
    val lowBus: Boolean? = null,
    val elevatorFirst: Boolean? = null
)

data class UserAccount (
    val userAccountId:Int? = null,
    val email: String? = null,
    val password: String? = null,
    val passwordSalt: String? = null,
    val hashAlgorithmId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val address: String? = null,
    val detailAddress: String? = null
)

class UserRelation (
    val userRelationId:Int? = null,
    val caretakerId:Int? = null,
    val caregiverId:Int? = null,
    val relation: String? = null
)

@Composable
fun EmergencySettingsScreen() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        // 최상단 보호자 (이름/주소/연락처)
        val caregiverUserAccount = UserAccount(1, "audwnss@naver.com", "qweasdmnb123", "qweasdmnb123", "hashAlgorithm", "2001-06-15", "2024-08-15")
        val caregiverUserSetting = UserSetting(1, true, true, true, false, false)
        val caregiverUser1 = User1(1, "김명준", "010-9032-0000", "명준", caregiverUserSetting, caregiverUserAccount)
        val caregiverUserRelation = UserRelation(11, 1, 2, "보호자")
        item{
            Divider("보호자 연락처")
            ItemRow(caregiverUserRelation.relation,caregiverUserAccount.address, caregiverUser1.telNum)
            Divider("긴급 연락처")
        }

        // 이후로 긴급 연락처
        val emrgList = listOf(
            Emergency(11,1, "y", "병원", "02-760-0000", "성북구 삼선교로"),
            Emergency(12,1, "y", "김상준", "010-9570-5249", "용산"),
            Emergency(13,1, "y", "이준희", "010-2916-2027", "홍대")
        )

        // 긴급연락처 추가하기
        items(emrgList){
            ItemRow(it.name, it.address, it.telNum)
        }

        // 버튼 누를 시 전화하기 + 이동하기
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.End) {
                Text("추가하기")
                Spacer(modifier = Modifier.width(10.dp))
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview
@Composable
fun EmergencySettingsPreview() {
    EmergencySettingsScreen()
}

@Composable
fun ItemRow(name:String?, address:String?, telNum:String?) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .drawBehind {
                val strokeWidth = 1 * density
                val y = size.height - strokeWidth / 2
                drawLine(
                    Color(240, 240, 240),
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
            .padding(horizontal = 12.dp, vertical = 12.dp)) {
        Text(
            text = name?:"None",
            modifier = Modifier.width(80.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = address?:"None",
            modifier = Modifier.width(185.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = telNum?:"None",
            modifier = Modifier.width(95.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}