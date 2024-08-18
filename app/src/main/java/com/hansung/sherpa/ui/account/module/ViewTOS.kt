package com.hansung.sherpa.ui.account.module

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.sherpares.SherpaCheckBoxColor
import com.hansung.sherpa.sherpares.SherpaColor

@Composable
fun ViewTOS(tosText:String, checked:Boolean, textColor: Color = Color.Black, modifier: Modifier, onCheckedChange: (Boolean) -> Unit) {
    val fontSize = 12.sp
    Row(
        modifier = modifier,
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