package com.hansung.sherpa.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.sherpares.PretendardVariable

@Composable
fun SherpaSchedule(
    title: String,
    description: String,
    dateBegin: String,
    dateEnd: String,
    onConfirmation: () -> Unit
) {
    Card(
        //modifier = Modifier.size(320.dp, 220.dp),
        modifier = Modifier.width(320.dp).heightIn(min = 220.dp),
        colors = CardColors(backgroundCardColor, backgroundCardColor, backgroundCardColor, backgroundCardColor),
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

@Preview
@Composable
fun SherpaSchedulePreview(){
    SherpaSchedule("일정 제목", "일정 내용 사과사기 qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", "2024-08-12 22:24", "2024-08-12 23:24") {}
}