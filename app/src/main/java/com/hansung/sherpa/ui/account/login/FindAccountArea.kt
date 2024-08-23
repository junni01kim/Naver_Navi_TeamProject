package com.hansung.sherpa.ui.account.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.sherpares.BmHanna

@Composable
fun FindAccountArea(navController: NavController) {
    // 비밀번호 찾기, 회원가입 이동
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = {},
            colors= ButtonColors(
                contentColor = Color.Black,
                containerColor = Color.Transparent,
                disabledContentColor = Color.Black,
                disabledContainerColor =  Color.Transparent
            ),
            modifier = Modifier.wrapContentWidth()
        ){
            Text(
                text = "아이디/비밀번호를 잃어버리셨나요?",
                fontSize = 18.sp,
                fontFamily = BmHanna,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
        TextButton(
            onClick = {navController.navigate("${SherpaScreen.SignUp.name}")},
            colors= ButtonColors(
                contentColor = Color(0xFF34DFD5),
                containerColor = Color.Transparent,
                disabledContentColor = Color(0xFF34DFD5),
                disabledContainerColor =  Color.Transparent
            ),
            modifier = Modifier.wrapContentWidth()
        ){
            Text(
                text = "계정이 없으신가요?",
                fontSize = 18.sp,
                fontFamily = BmHanna,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}