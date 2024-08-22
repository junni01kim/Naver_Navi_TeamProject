package com.hansung.sherpa.ui.account.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hansung.sherpa.sherpares.BmHanna
import com.hansung.sherpa.sherpares.SherpaColor
import com.hansung.sherpa.ui.account.module.InfomationGroup
import com.hansung.sherpa.ui.account.signup.isValidId

/**
 * 보호자 로그인 구성품
 */
@Composable
fun LoginArea(navController: NavController) {
    val context = LocalContext.current

    var idValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp, 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        InfomationGroup("이메일", false) {idValue = it.trim()}
        InfomationGroup("비밀번호", false) {passwordValue = it.trim()}

        TextButton(
            onClick = {
                if(!isValidId(idValue)){
                    Toast.makeText(context,"로그인 실패!\n아이디 서식을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                if(!isValidId(passwordValue)){
                    Toast.makeText(context,"로그인 실패!\n비밀번호 서식을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                if(login(navController, idValue, passwordValue)) {
                    Toast.makeText(context,"로그인 실패!\n아이디 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                    return@TextButton
                } },
            colors= ButtonColors(
                contentColor = Color.Black,
                containerColor = SherpaColor,
                disabledContentColor = Color.Black,
                disabledContainerColor =  SherpaColor
            ),
            modifier = Modifier.width(200.dp)
        ){
            Text(
                text = "로그인",
                fontFamily = BmHanna
            )
        }
    }
}