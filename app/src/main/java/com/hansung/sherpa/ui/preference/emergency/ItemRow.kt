package com.hansung.sherpa.ui.preference.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 설정 창 긴급 연락처 리스트 항목
 *
 * @param name 이름
 * @param address 주소
 * @param telNum 전화번호
 * @param deleteItem 삭제를 위한 람다함수 (상태 Hoisting)
 * @param showItem 정보 열람을 위한 람다함수 (상태 Hoisting)
 */
@Composable
fun ItemRow(name:String?, address:String?, telNum:String?, deleteItem: () -> Unit, showItem: () -> Unit) {
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
            // LongPress: 긴급 연락처 삭제, onTap: 긴급 연락처 정보 열람
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        deleteItem()
                    },
                    onTap = {
                        showItem()
                    }
                )
            }
            .padding(horizontal = 12.dp, vertical = 12.dp)) {
        // 긴급 연락처 이름
        Text(
            text = name ?: "None",
            modifier = Modifier.width(80.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        // 긴급 연락처 주소
        Text(
            text = address ?: "None",
            modifier = Modifier.width(175.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        // 긴급 연락처 전화번호
        Text(
            text = telNum ?: "None",
            modifier = Modifier.width(120.dp),
            maxLines = 1,
            textAlign = TextAlign.End
        )
    }
}