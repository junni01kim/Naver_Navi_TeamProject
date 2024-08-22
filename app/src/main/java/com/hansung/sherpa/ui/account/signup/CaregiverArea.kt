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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.sherpares.BmHanna
import com.hansung.sherpa.sherpares.SherpaButtonColor
import com.hansung.sherpa.sherpares.SherpaColor
import com.hansung.sherpa.ui.account.module.InfomationGroup
import com.hansung.sherpa.ui.account.module.ViewTOS
import com.hansung.sherpa.user.CreateUserRequest
import java.sql.Timestamp
import java.util.Calendar

/**
 * 보호자 회원가입 관련 구성품
 */
@Composable
fun CaregiverArea(navController: NavController){
    val context = LocalContext.current

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var confirmPasswordValue by remember { mutableStateOf("") }
    var userNameValue by remember { mutableStateOf("") }
    var telValue by remember { mutableStateOf("") }
    var addressValue by remember { mutableStateOf("") }
    var detailAddressValue by remember { mutableStateOf("") }

    var tosChecked by remember { mutableStateOf(false) }
    var marketingChecked by remember { mutableStateOf(false) }
    var allChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
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

        InfomationGroup("이메일", true, "중복검사", {/*중복검사 API*/}) { emailValue = it.trim() }
        InfomationGroup("비밀번호") { passwordValue = it.trim() }
        InfomationGroup("비밀번호\n확인") { confirmPasswordValue = it.trim() }
        InfomationGroup("이름") { userNameValue = it }
        InfomationGroup("전화번호", true, "인증하기", {/* 전화 인증 API */}) { telValue = it.trim() }
        InfomationGroup("주소", true, "주소검색", {/* 주소 검색 API */}) { addressValue = it }
        InfomationGroup("상세주소") { detailAddressValue = it }

        Spacer(modifier = Modifier.height(10.dp))

        ViewTOS(
            tosText = "이용약관에 동의합니다.",
            checked = tosChecked,
            modifier = Modifier
                .width(250.dp)
                .wrapContentHeight(),
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
            onCheckedChange = {
                allChecked = it
                tosChecked = it
                marketingChecked = it
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = {
                if(!allChecked){
                    Toast.makeText(context, "약관을 모두 동의해주세요.", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                if(!isValidId(emailValue)){
                    Toast.makeText(context,"로그인 실패!\n이메일 서식을 확인해주세요\n8~20 글자로 가능합니다.", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }
                if(!isValidId(passwordValue)){
                    Toast.makeText(context,"로그인 실패!\n비밀번호 서식을 확인해주세요\n8~20 글자로 가능합니다.", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                if(passwordValue != confirmPasswordValue){
                    Toast.makeText(context, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
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
                    createdAt = Timestamp(Calendar.getInstance().timeInMillis),
                    updatedAt = Timestamp(Calendar.getInstance().timeInMillis)
                )
                if(signup(navController, createUserRequest)){
                    Toast.makeText(context, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                }
            },
            colors= SherpaButtonColor,
            modifier = Modifier.width(200.dp)
        ){
            Text(
                text = "확인",
                fontSize = 15.sp,
                fontFamily = BmHanna
            )
        }
    }
}