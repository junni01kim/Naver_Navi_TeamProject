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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.dialog.SherpaDialogParm
import com.hansung.sherpa.sherpares.BmHanna
import com.hansung.sherpa.sherpares.SherpaButtonColor
import com.hansung.sherpa.sherpares.SherpaColor
import com.hansung.sherpa.ui.account.module.InfomationGroup
import com.hansung.sherpa.ui.account.module.ViewTOS
import com.hansung.sherpa.user.CreateUserRequest
import com.hansung.sherpa.user.UserManager
import java.sql.Timestamp
import java.util.Calendar

/**
 * 보호자 회원가입 관련 구성품
 */
@Composable
fun CaregiverArea(navController: NavController, sherpaDialog: MutableState<SherpaDialogParm>, showDialog: (Boolean) -> Unit){
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
                sherpaDialog.value = refuseSignup(
                    emailValue,
                    passwordValue,
                    confirmPasswordValue,
                    userNameValue,
                    telValue,
                    addressValue,
                    detailAddressValue,
                    allChecked,
                    showDialog
                )

                if(sherpaDialog.value.title != ""){
                    showDialog(true)
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
                val user1 = UserManager().create(createUserRequest)
                if(user1.code == 200) navController.navigate(SherpaScreen.Login.name)
                else {
                    sherpaDialog.value.setParm(
                        title = "회원가입 실패",
                        message =listOf("계정 생성을 실패하였습니다"),
                        confirmButtonText = "확인",
                        onConfirmation = { showDialog(false) },
                        onDismissRequest = { showDialog(false) }
                    )
                    showDialog(true)
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

fun refuseSignup(
    emailValue: String,
    passwordValue: String,
    confirmPasswordValue: String,
    userNameValue: String,
    telValue: String,
    addressValue: String,
    detailAddressValue: String,
    allChecked: Boolean,
    showDialog: (Boolean) -> Unit
): SherpaDialogParm {
    val sherpaDialog = SherpaDialogParm()

    if(passwordValue != confirmPasswordValue){
        sherpaDialog.setParm(
            title = "회원가입 실패",
            message =listOf("비밀번호가 다릅니다"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(!isValidId(passwordValue)){
        sherpaDialog.setParm(
            title = "회원가입 실패",
            message =listOf("비밀번호 서식을 확인해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(!isValidId(emailValue)){
        sherpaDialog.setParm(
            title = "회원가입 실패",
            message =listOf("이메일 서식을 확인해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(telValue == "") {
        sherpaDialog.setParm(
            title = "필수 입력",
            message =listOf("전화번호를 기입해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(addressValue == ""){
        sherpaDialog.setParm(
            title = "필수 입력",
            message =listOf("주소를 기입해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(userNameValue == ""){
        sherpaDialog.setParm(
            title = "필수 입력",
            message =listOf("이름을 기입해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(confirmPasswordValue == ""){
        sherpaDialog.setParm(
            title = "필수 입력",
            message =listOf("비밀번호 확인을 기입해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(passwordValue == "") {
        sherpaDialog.setParm(
            title = "필수 입력",
            message =listOf("비밀번호를 기입해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(emailValue == ""){
        sherpaDialog.setParm(
            title = "필수 입력",
            message =listOf("이메일을 기입해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    if(!allChecked){
        sherpaDialog.setParm(
            title = "회원가입 실패",
            message =listOf("약관을 모두 동의해주세요"),
            confirmButtonText = "확인",
            onConfirmation = { showDialog(false) },
            onDismissRequest = { showDialog(false) }
        )
    }

    return sherpaDialog
}
