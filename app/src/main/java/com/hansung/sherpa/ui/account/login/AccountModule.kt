package com.hansung.sherpa.ui.account.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
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
import com.hansung.sherpa.ui.theme.BmHanna
import com.hansung.sherpa.ui.theme.SherpaCheckBoxColor
import com.hansung.sherpa.ui.theme.SherpaColor

/**
 * account 패키지에서 빈번하게 사용되는 디자인들을 모듈화 시켜둔 파일
 *
 */
val rowWidth = 250.dp
val rowHeight = 30.dp
val titleTextWidth = 50.dp
val buttonWidth = 50.dp
val spacerWidth = 10.dp

/**
 * 정보 입력란을 모듈화 한 Composable
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

@Composable
fun AccountTextField(value:String, onValueChange: (String) -> Unit, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(1.dp, Color.Black, RoundedCornerShape(10.dp))
    ){
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = modifier
                .wrapContentHeight()
                .padding(horizontal = 10.dp)
        )
    }
}


@Composable
fun AccountButton(buttonText:String, buttonColor: Color, modifier: Modifier, onClick: () -> Unit){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .background(buttonColor)
            .clickable { onClick() }
    ){
        Text(
            text = buttonText,
            fontSize = 10.sp,
            fontFamily = BmHanna
        )
    }
}

/**
 * 이용약관 창 디자인을 모듈화 한 Composable
 *
 * @param tosText 이용약관 내용
 * @param checked 이용약관 버튼의 선택 여부
 * @param textColor 이용약관의 글씨 색상
 * @param onCheckedChange 체크 시 수행될 동작
 */
@Composable
fun ViewTOS(tosText:String, checked:Boolean, textColor: Color = Color.Black, onCheckedChange: (Boolean) -> Unit) {
    val fontSize = 12.sp
    Row(
        modifier = Modifier
            .width(250.dp)
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tosText,
            color = textColor,
            fontSize = fontSize
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "약관 보기",
            fontSize = fontSize,
            color = SherpaColor,
            modifier = Modifier.clickable { /*TODO: 약관 보기*/ }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            modifier = Modifier.size(20.dp),
            colors = SherpaCheckBoxColor
        )
    }
}