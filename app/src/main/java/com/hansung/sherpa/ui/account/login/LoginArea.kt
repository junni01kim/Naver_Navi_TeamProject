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
import androidx.compose.runtime.MutableState
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
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.ui.common.SherpaDialogParm
import com.hansung.sherpa.ui.theme.BmHanna
import com.hansung.sherpa.ui.theme.SherpaColor
import com.hansung.sherpa.ui.account.signup.isValidId
import com.hansung.sherpa.user.UserManager

/**
 * 로그인 화면을 구성하는 영역
 *
 * @param navController 홈 화면 navController 원형, ※ 화면을 이동한다면, 매개변수로 지정 필수
 * @param sherpaDialog SherpaDialog 상태 값을 가진 객체
 */
@Composable
fun LoginArea(
    navController: NavController,
    sherpaDialog: MutableState<SherpaDialogParm>
) {
    val context = LocalContext.current

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp, 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        /**
         * Email Input Area 이메일 입력란
         * Password Input Area 비밀번호 입력란
         */
        InfomationGroup("이메일", false) {emailValue = it.trim()}
        InfomationGroup("비밀번호", false) {passwordValue = it.trim()}

        /**
         * Login Button
         * 
         * 로그인을 진행하는 버튼
         */
        TextButton(
            onClick = {
                if(!isValidId(emailValue)||!isValidId(passwordValue)){
                    sherpaDialog.value.setParm (
                        title = "로그인 실패",
                        message = listOf("이메일/비밀번호를 확인해주세요")
                    )
                    return@TextButton
                }

                if(login(navController, emailValue, passwordValue)) {
                    Toast.makeText(context,"로그인 실패!\n아이디 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }
                      },
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

/**
 * 로그인을 진행하는 함수
 *
 * @param navController 홈 화면 navController 원형, ※ 화면을 이동한다면, 매개변수로 지정 필수
 * @param email 이메일
 * @param password 비밀번호
 */
fun login(navController: NavController, email: String, password: String) : Boolean {
    val loginResponse = UserManager().login(email, password)

    /**
     * 예외처리 에러 코드
     *
     * 에러코드 200: 로그인 성공 시 반환
     * TODO: 에러처리 추가
     */
    if(loginResponse.code == 200) {
        StaticValue.userInfo = loginResponse.data!!
        UserManager().updateFcm()
        navController.navigate(SherpaScreen.Home.name)
    }
    else {
        return true
    }
    return false
}
