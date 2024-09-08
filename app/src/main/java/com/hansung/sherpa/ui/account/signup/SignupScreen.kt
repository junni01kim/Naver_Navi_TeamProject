package com.hansung.sherpa.ui.account.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.ui.common.SherpaDialog
import com.hansung.sherpa.ui.common.SherpaDialogParm
import com.hansung.sherpa.ui.theme.BmHanna
import com.hansung.sherpa.ui.theme.SherpaColor
import com.hansung.sherpa.ui.account.login.rowWidth

/**
 * 회원가입을 진행하는 화면
 *
 * @param navController 홈 화면 navController 원형, ※ 화면을 이동한다면, 매개변수로 지정 필수
 * @property careToggle 회원가입 대상이 설정 될 Role1 (true: 사용자, false: 보호자)
 */
@Composable
fun SignupScreen(navController: NavController = rememberNavController(), modifier: Modifier = Modifier){
    var careToggle by remember { mutableStateOf(false) }
    val sherpaDialog = remember { mutableStateOf(SherpaDialogParm())}

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        /**
         * Sherpa Dialog
         *
         * 알림 메세지
         */
        if(sherpaDialog.value.show.value) {
            SherpaDialog(
                title = sherpaDialog.value.title,
                message = sherpaDialog.value.message,
                confirmButtonText = sherpaDialog.value.confirmButtonText,
                dismissButtonText = sherpaDialog.value.dismissButtonText,
                onConfirmation = sherpaDialog.value.onConfirmation,
                onDismissRequest = sherpaDialog.value.onDismissRequest
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .rotate(180f)
                    .clip(RoundedCornerShape(800.dp, 800.dp))
                    .background(SherpaColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(Color.White)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            TitleArea()

            Column {
                Row(
                    modifier = Modifier.width(rowWidth +20.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    Box(modifier = Modifier
                        .size(70.dp, 20.dp)
                        .clip(RoundedCornerShape(100, 100))
                        .background(Color.White)
                        .clickable {
                            careToggle = false
                        },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "보호자",
                            color = Color.Black,
                            fontFamily = BmHanna
                        )
                    }
                    Box(modifier = Modifier
                        .size(70.dp, 20.dp)
                        .clip(RoundedCornerShape(100, 100))
                        .background(Color.Black)
                        .clickable {
                            careToggle = true
                        },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "사용자",
                            fontFamily = BmHanna,
                            color = Color.White
                        )
                    }
                }
                /**
                 * 사용자 회원가입을 구성하는 영역 
                 */
                if (careToggle) CaretakerArea(navController, sherpaDialog)
                /**
                 * 보호자 회원가입을 구성하는 영역
                 */
                else CaregiverArea(navController, sherpaDialog)
            }
        }
    }
}

/**ㅂㅈ
 * 회원가입 서식 확인하는 함수
 */
fun isValidId(id: String): Boolean {
    val regex = "^[a-zA-Z0-9!@#\$%^&*()_+\\-=]{8,20}$".toRegex()
    return regex.matches(id)
}

/**
 * 회원가입 이메일 서식을 확인하는 함수
 */
fun isValidEmail(email: String): Boolean {
    val regex = "^[a-zA-Z0-9!@#\$%^&*()_+\\-=]{8,20}@[a-zA-Z0-9.-]{2,}+\\.[a-zA-Z]{2,}\$\n".toRegex()
    return regex.matches(email)
}

/**
 * 회원가입 비밀번호 서식을 확인하는 함수
 */
fun isValidPassword(id: String): Boolean {
    val regex = "^[a-zA-Z0-9!@#\$%^&*()_+\\-=]{8,20}@[a-zA-Z0-9.-]{2,}+\\.[a-zA-Z]{2,}\$\n".toRegex()
    return regex.matches(id)
}

@Composable
@Preview
fun SignupPreview() {
    SignupScreen()
}