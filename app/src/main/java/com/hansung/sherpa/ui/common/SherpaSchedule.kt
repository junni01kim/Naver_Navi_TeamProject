package com.hansung.sherpa.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hansung.sherpa.ui.theme.PretendardVariable

@Composable
fun SherpaSchedule(
    title: String,
    description: String,
    dateBegin: String,
    dateEnd: String,
    onConfirmation: () -> Unit
) {
    Dialog(
        onDismissRequest = onConfirmation
    ) {
        Card(
            //modifier = Modifier.size(320.dp, 220.dp),
            modifier = Modifier.width(320.dp).heightIn(min = 220.dp),
            colors = CardColors(
                backgroundCardColor,
                backgroundCardColor,
                backgroundCardColor,
                backgroundCardColor
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth().wrapContentHeight()
                    .padding(top = 30.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = nomalTextColor,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PretendardVariable
                )

                Text(
                    modifier = Modifier.padding(horizontal = 40.dp),
                    text = description,
                    textAlign = TextAlign.Center,
                    color = lightTextColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PretendardVariable
                )

                Text(
                    text = "$dateBegin ~ $dateEnd",
                    fontSize = 12.sp,
                    color = lightTextColor,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PretendardVariable
                )

                SherpaButton2(
                    confirmButtonText = "확인",
                    onConfirmation = onConfirmation
                )
            }
        }
    }
}

@Preview
@Composable
fun SherpaSchedulePreview(){
    SherpaSchedule("결과 보고서", "프로보노 결과 보고서 제출할 시간", "2024-09-01 12:24", "2024-09-01 12:30") {}
}