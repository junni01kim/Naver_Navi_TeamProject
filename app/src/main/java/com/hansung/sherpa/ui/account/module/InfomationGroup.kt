package com.hansung.sherpa.ui.account.module

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.sherpares.BmHanna
import com.hansung.sherpa.sherpares.SherpaColor

/**
 * 나중에 로그인 만드실 분 생각해서 모듈화해둠 알아서 변경해서 쓸 것
 *
 * @param titleText
 * @param buttonToggle
 * @param buttonText 버튼에 들어갈 텍스트 ex) (아이디) 중복검사
 * @param modifier Row modifier와 연결 되어있다. 혹시 설정하고 싶으면 하시오.
 * @param buttonClick 람다함수. buttonToggle을 true로 설정했을 때 사용하면 된다. 버튼 눌렀을 때 필요한 동작 하면됨
 * @param update 연결시켜둔 문자열에 최종적으로 값이 들어감.
 *
 * @sample InfomationGroupSample
 */
val rowWidth = 250.dp
val rowHeight = 30.dp
val titleTextWidth = 50.dp
val buttonWidth = 50.dp
val spacerWidth = 10.dp

@Composable
fun InfomationGroup(titleText:String, buttonToggle:Boolean = false, buttonText:String = "", buttonClick: (String) -> Unit = {}, modifier: Modifier = Modifier, update: (String) -> Unit) {

    var value by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = titleText,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(titleTextWidth),
            fontSize = 12.sp,
            fontFamily = BmHanna,
        )
        Spacer(modifier = Modifier.width(spacerWidth))
        AccountTextField(
            value = value,
            onValueChange = {
                value = it
                update(value)
            },
            modifier = Modifier.size(
                if(buttonToggle) rowWidth - titleTextWidth - buttonWidth - spacerWidth
                else rowWidth - titleTextWidth, rowHeight
            )
        )
        if(buttonToggle){
            Spacer(modifier = Modifier.width(spacerWidth))

            AccountButton(
                buttonText = buttonText,
                buttonColor = SherpaColor,
                modifier = Modifier.size(buttonWidth, rowHeight)
            ){
                buttonClick(value)
            }
        }
    }
}

@Composable
fun InfomationGroupDarkMode(titleText:String, buttonToggle:Boolean = false, buttonText:String = "", buttonClick: (String) -> Unit = {}, modifier: Modifier = Modifier, update: (String) -> Unit) {

    var value by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = titleText,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.width(titleTextWidth),
            fontSize = 12.sp,
            fontFamily = BmHanna,
        )
        Spacer(modifier = Modifier.width(spacerWidth))
        AccountTextField(
            value = value,
            onValueChange = {
                value = it
                update(value)
            },
            modifier = Modifier.size(
                if(buttonToggle) rowWidth - titleTextWidth - buttonWidth - spacerWidth
                else rowWidth - titleTextWidth, rowHeight
            ).clip(RoundedCornerShape(10.dp)).background(Color.White)
        )
        if(buttonToggle){
            Spacer(modifier = Modifier.width(spacerWidth))

            AccountButton(
                buttonText = buttonText,
                buttonColor = SherpaColor,
                modifier = Modifier.size(buttonWidth, rowHeight)
            ){
                buttonClick(value)
            }
        }
    }
}