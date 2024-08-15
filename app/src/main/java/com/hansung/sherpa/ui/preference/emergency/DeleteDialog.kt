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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.hansung.sherpa.ui.theme.PurpleGrey40

/**
 * 긴급 연락처 삭제에 이용할 다이얼로그
 *
 * @param name 긴급 연락처 이름
 * @param onCloseRequest 화면을 닫을 때 이용하는 람다 함수. 상태 Hoisting에 이용한다.
 * @param onDeleteRequest 긴급 연락처 삭제에 이용할 람다 함수. ※ API를 이용할 때 필요없어 질 함수이다.
 */
@Composable
fun DeleteDialogUI(
    name:String?,
    onCloseRequest: () -> Unit,
    onDeleteRequest : () -> Unit
){
    // 긴급 연락처 추가 Dialog (김상준 팀원 코드 인용)
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
            .fillMaxSize().zIndex(2f),
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
                        text = "긴급 연락처 삭제",
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
                     * 하단에 삭제할 긴급 연락처의 name이 나타난다.
                     */
                    Text(
                        text = "연락처를 삭제 하시겠습니까?\n ${name}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.padding(4.dp))
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(lightGrayColor),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    TextButton(
                        onClick = { onCloseRequest() }
                    ) {
                        // 긴급 연락처 삭제 취소 버튼
                        Text(
                            "취소",
                            fontWeight = FontWeight.Bold,
                            color = PurpleGrey40,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                    TextButton(
                        onClick = { onDeleteRequest() }
                    ) {
                        // 긴급 연락처 삭제 버튼
                        Text(
                            "삭제",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(219, 0, 35),
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
            }
        }
    }
}