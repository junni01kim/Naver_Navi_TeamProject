package com.hansung.sherpa.ui.preference.usersetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordModal(
    callback : (Boolean) -> Unit
){
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = {  },
        dragHandle = {},
        modifier = Modifier
            .fillMaxHeight(0.965f),
    ) {
        PasswordModalHeader()
    }
}

@Composable
fun PasswordModalHeader(){
    val textStyle : TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 238, 248))
            .clip(RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                )
        ) {
            Text(
                text = "비밀번호 변경",
                modifier = Modifier
                    .padding(top = (12).dp)
                    .align(Alignment.TopCenter),
                style = textStyle
            )
            TextButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = {  }
            ) {
                Text(
                    text = "취소",
                    color = Color(219, 0, 35),
                    style = textStyle
                )
            }
        }
    }
}

@Composable
@Preview
fun PasswordPreview(){
    PasswordModalHeader()
}

