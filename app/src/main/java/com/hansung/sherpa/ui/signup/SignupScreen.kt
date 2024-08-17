package com.hansung.sherpa.ui.signup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.ui.login.SherpaColor
import com.hansung.sherpa.ui.login.bmHanna
import com.hansung.sherpa.user.createuser.CreateUserRequest
import com.hansung.sherpa.user.UserManager
import java.sql.Timestamp
import java.util.Calendar



@Composable
fun SignupScreen(navController: NavController = rememberNavController(), modifier: Modifier = Modifier){
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 800.dp, 800.dp))
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
            TitleArea()

            // 보호자 입력란
            CaregiverArea(navController)

            // 사용자 입력란
            //CaretakerArea(navController)
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
            fontFamily = bmHanna
        )
    }
}

/**
 * 보호자 회원가입 관련 구성품
 */
@Composable
fun CaregiverArea(navController: NavController){
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var confirmPasswordValue by remember { mutableStateOf("") }
    var userNameValue by remember { mutableStateOf("") }
    var telValue by remember { mutableStateOf("") }
    var addressValue by remember { mutableStateOf("") }
    var detailAddressValue by remember { mutableStateOf("") }

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
            fontFamily = bmHanna,
            fontSize = 20.sp,
            style = TextStyle(SherpaColor)
        )
        InfomationGroup("아이디", true, "중복검사", {/*중복검사 API*/}) { emailValue = it }
        InfomationGroup("비밀번호") { passwordValue = it }
        InfomationGroup("비밀번호 확인") { confirmPasswordValue = it }
        InfomationGroup("이름") { userNameValue = it }
        InfomationGroup("전화번호", true, "인증하기", {/* 전화 인증 API */}) { telValue = it }
        InfomationGroup("주소", true, "주소검색", {/* 주소 검색 API */}) { addressValue = it }
        InfomationGroup("상세주소") { detailAddressValue = it }
        Text("이용약관1")
        Text("이용약관2")
        Text("이용약관3")
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            // TODO: 로그인 정보로 보호자 역할 분기해야 됨
            onClick = {
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
            colors= ButtonColors(
                contentColor = Color.Black,
                containerColor = SherpaColor,
                disabledContentColor = Color.Black,
                disabledContainerColor =  SherpaColor
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

/**
 * 사용자 회원가입 구성품
 */
@Composable
fun CaretakerArea(navController: NavController) {
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

    Column {
        // TODO: 값 전송 시 공백 제거할 것
        InfomationGroup("이메일", true, "중복검사", {/*중복검사 API*/}) { emailValue = it }
        InfomationGroup("비밀번호", false) { passwordValue = it }
        InfomationGroup("비밀번호 확인", false) { confirmPasswordValue = it }
        InfomationGroup("이름", false) { userNameValue = it }
        InfomationGroup("전화번호", true, "인증하기", {/* 전화 인증 API */}) { telValue = it }
        InfomationGroup("주소", true, "주소검색", {/* 주소 검색 API */}) { addressValue = it }
        InfomationGroup("상세주소", false) { detailAddressValue = it }
        InfomationGroup("보호자 이메일", true, "연동하기" ,{
            // TODO: 보호자 연동 허가 로직 구현할 것
            // TODO: ※ (2024-08-12) 지금은 테스트용으로 보호자 승인 없이 DB에서 바로 받아옴. 진행

            caregiverId = UserManager().linkPermission(caregiverEmail)?.data!!
            caregiverRelation = "연동 로직 구현 시에는 이거도 같이 반환 받아야 함."
        }) { caregiverEmail = it }
        Text("이용약관1")
        Text("이용약관2")
        Text("이용약관3")
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            // TODO: 로그인 정보로 보호자 역할 분기해야 됨
            onClick = {
                if(caregiverId == -1) {
                    Log.d("explain", "보호자 연동 후 사용할 것")
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
@Composable
fun InfomationGroup(titleText:String, buttonToggle:Boolean = false, buttonText:String = "", buttonClick: (String) -> Unit = {}, modifier:Modifier = Modifier, update: (String) -> Unit) {
    val rowWidth = 250.dp
    val titleTextWidth = 60.dp
    val buttonWidth = 50.dp
    val spacerWidth = 10.dp

    var value by remember { mutableStateOf("")}

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = titleText,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(titleTextWidth),
            fontSize = 15.sp,
            fontFamily = bmHanna,
        )
        Spacer(modifier = Modifier.width(spacerWidth))
        SignupTextField(
            value = value,
            onValueChange = {
                value = it
                update(value)
            },
            modifier = Modifier.size(
                if(buttonToggle) rowWidth - titleTextWidth - buttonWidth - spacerWidth
                else rowWidth - titleTextWidth, 30.dp
            )
        )
        if(buttonToggle){
            Spacer(modifier = Modifier.width(spacerWidth))
            TextButton(
                onClick = { buttonClick(value) },
                modifier = Modifier.size(buttonWidth, 30.dp),
                colors = ButtonColors(
                    contentColor = Color.Black,
                    containerColor = SherpaColor,
                    disabledContentColor = Color.Black,
                    disabledContainerColor =  SherpaColor
                )
            ) {
                Text(
                    text = buttonText,
                    fontSize = 7.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SignupTextField(value:String, onValueChange: (String) -> Unit, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(1.dp,Color.Black, RoundedCornerShape(10.dp))
    ){
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = modifier.wrapContentHeight().padding(horizontal = 10.dp)
        )
    }
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