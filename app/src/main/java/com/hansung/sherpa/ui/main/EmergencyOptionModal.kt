package com.hansung.sherpa.ui.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private val TitleStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)

/**
 * < 화살표를 눌렀을 때 띄워지는 긴급 연락처의 모달창
 * - 전화하기 : 해당 연락처의 주인으로 전화 화면으로 넘어간다.
 * - 이동하기 : 해당 연락처의 주인으로 네비게이션 안내를 해준다.
 *
 * @param openDialog
 * @param contact
 */
@Composable
fun EmergencyOptionModal(openDialog: MutableState<Boolean> = remember { mutableStateOf(false) },  contact: Contact) {
    val onDismissRequest = { openDialog.value = false }
    val dialogWidth = 500.dp
    val dialogHeight = 300.dp
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(modifier = Modifier
            .height(dialogHeight)
            .width(dialogWidth),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier    = Modifier.fillMaxWidth(),
                    text        = contact.name,
                    textAlign   = TextAlign.Center,
                    style       = TitleStyle
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "연락처: ${contact.phone}")
                    Text(text = "주소: ${contact.address}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = "설명: ${contact.name}입니다.") // TODO 관계를 받아와야 됨.
                }
                EmergencyOptionButton(onDismissRequest, contact.phone)
            }
        }
    }
}

/**
 * < 눌렀을 때 나오는 모달창
 * 전화하기, 이동하기 모달
 *
 * @param onDismissRequest
 * @param phone
 */
@Composable
fun EmergencyOptionButton(onDismissRequest: () -> Unit = {}, phone: String = "010-0000-0000") {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        OutlinedButton(onClick = {
            onDismissRequest()
            val phoneNumber = "tel:${phone}"
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse(phoneNumber)
            }
            context.startActivity(intent)
        }) {
            Text(text = "전화하기")
        }
        FilledTonalButton(onClick = onDismissRequest) {
            Text("이동하기")
        }
    }
}