package com.hansung.sherpa.ui.account.signup

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.ui.common.SherpaDialogParm
import com.hansung.sherpa.ui.theme.BmHanna
import com.hansung.sherpa.ui.theme.SherpaButtonColor
import com.hansung.sherpa.ui.theme.SherpaColor
import com.hansung.sherpa.ui.account.login.InfomationGroup
import com.hansung.sherpa.ui.account.login.ViewTOS
import com.hansung.sherpa.user.CreateUserRequest
import com.hansung.sherpa.user.UserManager
import java.sql.Timestamp
import java.util.Calendar

/**
 * 보호자 회원가입을 구성하는 영역
 *
 * @param navController 홈 화면 navController 원형, ※ 화면을 이동한다면, 매개변수로 지정 필수
 * @param sherpaDialog SherpaDialog 상태 값을 가진 객체
 *
 */
@Composable
fun CaregiverArea(navController: NavController, sherpaDialog: MutableState<SherpaDialogParm>){
    /**
     * @property emailValue 이메일
     * @property passwordValue 비밀번호
     * @property confirmPasswordValue 비밀번호 확인
     * @property userNameValue 보호자 이름
     * @property telValue 전화번호
     * @property addressValue 주소
     * @property detailAddressValue 상세주소
     */
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var confirmPasswordValue by remember { mutableStateOf("") }
    var userNameValue by remember { mutableStateOf("") }
    var telValue by remember { mutableStateOf("") }
    var addressValue by remember { mutableStateOf("") }
    var detailAddressValue by remember { mutableStateOf("") }

    /**
     * @property tosChecked 이용 약관 확인
     * @property marketingChecked 마케팅 약관 확인
     * @property allChecked 전체 약관 확인
     */
    var tosChecked by remember { mutableStateOf(false) }
    var marketingChecked by remember { mutableStateOf(false) }
    var allChecked by remember { mutableStateOf(false) }

    /**
     * @property isEmailDuplicate 이메일 중복 확인
     * @property isTelNumDuplicate 전화번호 중복 확인
     */
    var isEmailDuplicate by remember { mutableStateOf(false) }
    var isTelNumDuplicate by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        /**
         * Infomation Area Text
         *
         * 정보 입력 텍스트
         */
        Text(
            text ="정보 입력",
            fontFamily = BmHanna,
            fontSize = 20.sp,
            style = TextStyle(SherpaColor)
        )
        /**
         * Infomation Area
         * 
         * 정보 입력 영역
         */
        InfomationGroup("이메일", true, "중복검사", {
            /**
             * 이메일 중복 검사 로직
             *
             * 에러처리 에러 코드
             *
             * 에러코드 200: API 연결은 성공했으나, 이메일이 중복된 경우
             * 에러코드 201: 이메일 이용가능
             *
             * // TODO: 에러처리 추가
             */
            val code = UserManager().verificatonEmail(emailValue).code
            if(code == 200) {
                sherpaDialog.value.setParm(
                    title = "이메일 중복",
                    message =listOf("이미 등록된 이메일입니다")
                )
                isEmailDuplicate = false
            }
            else if(code == 201) {
                sherpaDialog.value.setParm(
                    title = "이메일 승인",
                    message =listOf("사용가능한 이메일입니다")
                )
                isEmailDuplicate = true
            }
        }) { emailValue = it.trim() } // 이메일 텍스트 변경 시 양 끝 공백 제거
        InfomationGroup("비밀번호") { passwordValue = it.trim() }
        InfomationGroup("비밀번호\n확인") { confirmPasswordValue = it.trim() }
        InfomationGroup("이름") { userNameValue = it }
        InfomationGroup("전화번호", true, "인증하기", {
            /**
             * 전화번호 중복 검사 로직
             * 
             * 에러처리 에러 코드
             *
             * 에러코드 200: API 접속은 성공했으나, 전화번호가 중복된 경우
             * 에러코드 201: 전화번호 이용가능
             * // TODO: 에러처리 추가
             */
            val code = UserManager().verificatonTelNum(telValue).code
            if(code == 200) {
                sherpaDialog.value.setParm(
                    title = "전화번호 중복",
                    message =listOf("이미 등록된 전화번호입니다")
                )
                isTelNumDuplicate = false
            }
            else if(code == 201) {
                sherpaDialog.value.setParm(
                    title = "전화번호 승인",
                    message =listOf("사용가능한 전화번호입니다")
                )
                isTelNumDuplicate = true
            }
        }) { telValue = it.trim() } // 전화번호 텍스트 변경 시 양 끝 공백 제거
        InfomationGroup("주소", true, "주소검색", {/* 주소 검색 API */}) { addressValue = it }
        InfomationGroup("상세주소") { detailAddressValue = it }

        Spacer(modifier = Modifier.height(10.dp))

        /**
         * TOS Area
         *
         * 약관 확인 영역
         */
        ViewTOS(
            tosText = "이용약관에 동의합니다.",
            checked = tosChecked,
            onCheckedChange = {
                tosChecked = it
                allChecked = if(tosChecked && marketingChecked) true else false
            }
        )
        ViewTOS(
            tosText = "마케팅에 동의합니다.",
            checked = marketingChecked,
            onCheckedChange = {
                marketingChecked = it
                allChecked = if(tosChecked && marketingChecked) true else false
            }
        )
        ViewTOS(
            tosText = "해당 약관에 모두 동의합니다.",
            checked = allChecked,
            onCheckedChange = {
                allChecked = it
                tosChecked = it
                marketingChecked = it
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        /**
         * Confirm Button
         * 
         * 회원가입 신청 버튼
         */
        TextButton(
            onClick = {
                // 회원가입 에러처리 코드
                if(refuseSignup(
                        sherpaDialog,
                        emailValue,
                        passwordValue,
                        confirmPasswordValue,
                        userNameValue,
                        telValue,
                        addressValue,
                        detailAddressValue,
                        allChecked
                    )){
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

                /**
                 * 에러처리 에러 코드
                 *
                 * 에러코드 200: 보호자 생성 성공 시 반환
                 * // TODO: 에러처리 추가
                 */
                val user1 = UserManager().create(createUserRequest)
                if(user1.code == 200) navController.navigate(SherpaScreen.Login.name)
                else {
                    sherpaDialog.value.setParm(
                        title = "회원가입 실패",
                        message =listOf("계정 생성을 실패하였습니다"),
                        confirmButtonText = "확인"
                    )
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

/**
 * 회원가입 예외처리를 검사하는 함수
 *
 * @param sherpaDialog SherpaDialog 설정하기 위한 변수
 * @param emailValue 이메일
 * @param passwordValue 비밀번호
 * @param confirmPasswordValue 비밀번호 확인
 * @param userNameValue 보호자 이름
 * @param telValue 전화번호
 * @param addressValue 주소
 * @param detailAddressValue 상세 주소
 * @param allChecked 전체 약관 동의 여부
 *
 */
fun refuseSignup(
    sherpaDialog: MutableState<SherpaDialogParm>,
    emailValue: String,
    passwordValue: String,
    confirmPasswordValue: String,
    userNameValue: String,
    telValue: String,
    addressValue: String,
    detailAddressValue: String,
    allChecked: Boolean
): Boolean {
    if(passwordValue != confirmPasswordValue){
        sherpaDialog.value.setParm(
            title = "회원가입 실패",
            message =listOf("비밀번호가 다릅니다")
        )
        return true
    }

    if(!isValidId(passwordValue)){
        sherpaDialog.value.setParm(
            title = "회원가입 실패",
            message =listOf("비밀번호 서식을 확인해주세요")
        )
        return true
    }

    if(!isValidId(emailValue)){
        sherpaDialog.value.setParm(
            title = "회원가입 실패",
            message =listOf("이메일 서식을 확인해주세요")
        )
        return true
    }

    if(telValue == "") {
        sherpaDialog.value.setParm(
            title = "필수 입력",
            message =listOf("전화번호를 기입해주세요")
        )
        return true
    }

    if(addressValue == ""){
        sherpaDialog.value.setParm(
            title = "필수 입력",
            message =listOf("주소를 기입해주세요")
        )
        return true
    }

    if(userNameValue == ""){
        sherpaDialog.value.setParm(
            title = "필수 입력",
            message =listOf("이름을 기입해주세요")
        )
        return true
    }

    if(confirmPasswordValue == ""){
        sherpaDialog.value.setParm(
            title = "필수 입력",
            message =listOf("비밀번호 확인을 기입해주세요")
        )
        return true
    }

    if(passwordValue == "") {
        sherpaDialog.value.setParm(
            title = "필수 입력",
            message =listOf("비밀번호를 기입해주세요")
        )
        return true
    }

    if(emailValue == ""){
        sherpaDialog.value.setParm(
            title = "필수 입력",
            message =listOf("이메일을 기입해주세요")
        )
        return true
    }

    if(!allChecked){
        sherpaDialog.value.setParm(
            title = "회원가입 실패",
            message =listOf("약관을 모두 동의해주세요"),
        )
        return true
    }

    return false
}
