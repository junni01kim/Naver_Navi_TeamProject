package com.hansung.sherpa.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hansung.sherpa.R

private val TitlePadding = PaddingValues(top = 8.dp, bottom = 16.dp)
private val TitleStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)

@Composable
fun AddEmergencyContactFAB() {
    val openAlertDialog = remember { mutableStateOf(false) }
    val onDismissRequest = { openAlertDialog.value = false }
    val onAddRequest = { openAlertDialog.value = true }

    FloatingActionButton(onClick = onAddRequest) {
        CustomIcon(IconType.Resource(R.drawable.add), "")
    }
    when {
        openAlertDialog.value -> {
            AddEmergencyContactPopup(onDismissRequest)
        }
    }
}

/**
 * TODO
 * 긴급 전화 추가 팝업창
 */
@Composable
fun AddEmergencyContactPopup(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AddEmergencyContactTitle()
                AddEmergencyContactContents()
                AddEmergencyContactButtons(onDismissRequest)
            }
        }
    }
}

@Composable
fun AddEmergencyContactTitle() {
    val mergedStyle = LocalTextStyle.current.merge(TitleStyle)
    CompositionLocalProvider(
        LocalContentColor provides titleContentColor,
        LocalTextStyle provides mergedStyle,
        content = {
            Box(
                // Align the title to the center when an icon is present.
                Modifier
                    .padding(TitlePadding)
            ) {
                Text(text = "긴급 전화 추가")
            }
        }
    )
}

/**
 * TODO 팝업 창 내부 콘텐츠 채워야 함
 *
 */
@Composable
fun AddEmergencyContactContents() {
    Box(Modifier.height(75.dp)) {}
}

@Composable
fun AddEmergencyContactButtons(onDismissRequest: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement= Arrangement.SpaceAround) {
        TextButton(onClick = { /*TODO*/ }) {
            Text("추가", color = Color.Blue, fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onDismissRequest) {
            Text("취소", color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}