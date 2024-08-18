package com.hansung.sherpa.ui.account.signup

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.sherpares.BmHanna
import com.hansung.sherpa.sherpares.SherpaButtonColor
import com.hansung.sherpa.sherpares.SherpaColor
import com.hansung.sherpa.ui.account.module.InfomationGroup
import com.hansung.sherpa.ui.account.module.InfomationGroupDarkMode
import com.hansung.sherpa.ui.account.module.ViewTOS
import com.hansung.sherpa.ui.account.module.rowWidth
import com.hansung.sherpa.user.createuser.CreateUserRequest
import com.hansung.sherpa.user.UserManager
import java.sql.Timestamp
import java.util.Calendar

@Composable
fun SignupScreen(navController: NavController = rememberNavController(), modifier: Modifier = Modifier){
    var careToggle by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
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

            // 보호자 입력란
            Column {
                Row(
                    modifier = Modifier.width(rowWidth+20.dp),
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
                if (careToggle) CaretakerArea(navController)
                else CaregiverArea(navController)
            }
        }
    }
}

@Composable
fun TitleArea() {
    Row(verticalAlignment = Alignment.CenterVertically){
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "디자인을 위한 사람 아이콘",
            modifier = Modifier.size(60.dp,60.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "회원가입",
            fontSize = 40.sp,
            fontFamily = BmHanna
        )
    }
}

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
                    Toast.makeText(context,"로그인 실패!\n이메일 서식을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                if(!isValidId(passwordValue)){
                    Toast.makeText(context,"로그인 실패!\n비밀번호 서식을 확인해주세요", Toast.LENGTH_SHORT).show()
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
                signup(navController, createUserRequest)
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
            // TODO: 보호자 연동 허가 로직 구현할 것
            // TODO: ※ (2024-08-12) 지금은 테스트용으로 보호자 승인 없이 DB에서 바로 받아옴. 진행

            caregiverId = UserManager().linkPermission(caregiverEmail)?.data!!
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
                signup(navController, createUserRequest)
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

fun isValidId(id: String): Boolean {
    val regex = "^[a-zA-Z0-9!@#\$%^&*()_+\\-=]{8,20}$".toRegex()
    return regex.matches(id)
}

fun signup(navController: NavController, createUserRequest: CreateUserRequest) {
    UserManager().create(createUserRequest)
    navController.navigate("${SherpaScreen.Login.name}")
}

@Composable
@Preview
fun SignupPreview() {
    SignupScreen()
}