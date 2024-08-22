package com.hansung.sherpa.ui.account.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.sherpares.BmHanna
import com.hansung.sherpa.sherpares.SherpaColor
import com.hansung.sherpa.ui.account.module.InfomationGroupDarkMode
import com.hansung.sherpa.ui.account.module.ViewTOS
import com.hansung.sherpa.user.CreateUserRequest
import com.hansung.sherpa.user.UserManager
import java.sql.Timestamp
import java.util.Calendar

/**
 * 사용자 회원가입 구성품
 */
@Composable
fun CaretakerArea(navController: NavController) {
    val context = LocalContext.current

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var confirmPasswordValue by remember { mutableStateOf("") }
    var userNameValue by remember { mutableStateOf("") }
    var telValue by remember { mutableStateOf("") }
    var addressValue by remember { mutableStateOf("") }
    var detailAddressValue by remember { mutableStateOf("") }
    var caregiverId by remember { mutableStateOf(-1) }
    var caregiverEmail by remember { mutableStateOf("-1") }
    var caregiverRelation by remember { mutableStateOf("보호자") }

    var tosChecked by remember { mutableStateOf(false) }
    var marketingChecked by remember { mutableStateOf(false) }
    var allChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
            .padding(20.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text ="정보 입력",
            fontFamily = BmHanna,
            fontSize = 20.sp,
            style = TextStyle(SherpaColor)
        )

        InfomationGroupDarkMode("이메일", true, "중복검사", {/*중복검사 API*/}) { emailValue = it.trim() }
        InfomationGroupDarkMode("비밀번호", false) { passwordValue = it.trim() }
        InfomationGroupDarkMode("비밀번호\n확인", false) { confirmPasswordValue = it.trim() }
        InfomationGroupDarkMode("이름", false) { userNameValue = it }
        InfomationGroupDarkMode("전화번호", true, "인증하기", {/* 전화 인증 API */}) { telValue = it.trim() }
        InfomationGroupDarkMode("주소", true, "주소검색", {/* 주소 검색 API */}) { addressValue = it }
        InfomationGroupDarkMode("상세주소", false) { detailAddressValue = it }
        InfomationGroupDarkMode("보호자\n이메일", true, "연동하기" ,{
            // TODO: ※ (2024-08-12) 지금은 테스트용으로 보호자 승인 없이 DB에서 바로 받아옴. 진행

            val caregiverUserResponse = UserManager().linkPermission(caregiverEmail)

            if(caregiverUserResponse?.code == 404) {
                Toast.makeText(context, "일치하는 보호자 아이디가 없습니다.", Toast.LENGTH_SHORT).show()
                return@InfomationGroupDarkMode
            }

            // TODO: 여기에 보호자 연동 허가 로직 구현할 것

            caregiverId = caregiverUserResponse?.data!!

            caregiverRelation = "연동 로직 구현 시에는 이거도 같이 반환 받아야 함."
        }) { caregiverEmail = it.trim() }

        Spacer(modifier = Modifier.height(10.dp))

        ViewTOS(
            tosText = "이용약관에 동의합니다.",
            checked = tosChecked,
            modifier = Modifier
                .width(250.dp)
                .wrapContentHeight(),
            textColor = Color.White,
            onCheckedChange = {
                tosChecked = it
                allChecked = if(tosChecked && marketingChecked) true else false
            }
        )
        ViewTOS(
            tosText = "마케팅에 동의합니다.",
            checked = marketingChecked,
            modifier = Modifier
                .width(250.dp)
                .wrapContentHeight(),
            textColor = Color.White,
            onCheckedChange = {
                marketingChecked = it
                allChecked = if(tosChecked && marketingChecked) true else false
            }
        )
        ViewTOS(
            tosText = "해당 약관에 모두 동의합니다.",
            checked = allChecked,
            modifier = Modifier
                .width(250.dp)
                .wrapContentHeight(),
            textColor = Color.White,
            onCheckedChange = {
                allChecked = it
                tosChecked = it
                marketingChecked = it
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            // TODO: 로그인 정보로 보호자 역할 분기해야 됨
            onClick = {
                if(!allChecked){
                    Toast.makeText(context, "약관을 모두 동의해주세요.", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                if(!isValidId(emailValue)){
                    Toast.makeText(context,"로그인 실패!\n이메일 서식을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                if(!isValidId(passwordValue)){
                    Toast.makeText(context,"로그인 실패!\n비밀번호 서식을 확인해주세요", Toast.LENGTH_SHORT).show()
                }

                if(passwordValue != confirmPasswordValue){
                    Toast.makeText(context, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                if(caregiverId == -1) {
                    Toast.makeText(context, "보호자 연동 후 사용할 것", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                val createUserRequest = CreateUserRequest(
                    email = emailValue,
                    password = passwordValue,
                    name = userNameValue,
                    telNum = telValue,
                    address = addressValue,
                    detailAddress = detailAddressValue,
                    fcmToken = StaticValue.FcmToken,
                    caregiverId = caregiverId,
                    caregiverRelation = caregiverRelation,
                    createdAt = Timestamp(Calendar.getInstance().timeInMillis),
                    updatedAt = Timestamp(Calendar.getInstance().timeInMillis)
                )

                if(signup(navController, createUserRequest)){
                    Toast.makeText(context, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                }
            },
            colors= ButtonColors(
                contentColor = Color.Black,
                containerColor = Color(0xFF64FCD9),
                disabledContentColor = Color.Black,
                disabledContainerColor =  Color(0xFF64FCD9)
            ),
            modifier = Modifier.width(200.dp)
        ){
            Text(
                text = "확인",
                fontWeight = FontWeight.Bold
            )
        }
    }
}