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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.ui.common.SherpaDialogParm
import com.hansung.sherpa.ui.theme.BmHanna
import com.hansung.sherpa.ui.theme.SherpaColor
import com.hansung.sherpa.ui.account.login.InfomationGroupDarkMode
import com.hansung.sherpa.ui.account.login.ViewTOS
import com.hansung.sherpa.user.CreateUserRequest
import com.hansung.sherpa.user.UserManager
import java.sql.Timestamp
import java.util.Calendar

/**
 * 사용자 회원가입을 구성하는 영역
 *
 * @param navController 홈 화면 navController 원형, ※ 화면을 이동한다면, 매개변수로 지정 필수
 * @param sherpaDialog SherpaDialog 상태 값을 가진 객체
 */
@Composable
fun CaretakerArea(navController: NavController, sherpaDialog: MutableState<SherpaDialogParm>) {
    /**
     * @property emailValue 이메일
     * @property passwordValue 비밀번호
     * @property confirmPasswordValue 비밀번호 확인
     * @property userNameValue 보호자 이름
     * @property telValue 전화번호
     * @property addressValue 주소
     * @property detailAddressValue 상세주소
     * @property caregiverId 보호자 userId
     * @property caregiverEmail 보호자 이메일
     * @property caregiverRelation 보호자 관계(이름)
     */
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var confirmPasswordValue by remember { mutableStateOf("") }
    var userNameValue by remember { mutableStateOf("") }
    var telValue by remember { mutableStateOf("") }
    var addressValue by remember { mutableStateOf("") }
    var detailAddressValue by remember { mutableStateOf("") }
    var caregiverId by remember { mutableIntStateOf(-1) }
    var caregiverEmail by remember { mutableStateOf("-1") }
    var caregiverRelation by remember { mutableStateOf("보호자") }

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
            .background(Color.Black)
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
        InfomationGroupDarkMode("이메일", true, "중복검사", {
            /**
             * 이메일 중복 검사 로직
             *
             * 에러처리 에러 코드
             *
             * 에러코드 200: API 연결은 성공했으나, 이메일이 중복된 경우
             * 에러코드 201: 이메일 이용가능
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
        InfomationGroupDarkMode("비밀번호", false) { passwordValue = it.trim() }
        InfomationGroupDarkMode("비밀번호\n확인", false) { confirmPasswordValue = it.trim() }
        InfomationGroupDarkMode("이름", false) { userNameValue = it }
        InfomationGroupDarkMode("전화번호", true, "인증하기", {
            /**
             * 전화번호 중복 검사 로직
             *
             * 에러처리 에러 코드
             *
             * 에러코드 200: API 연결은 성공했으나, 전화번호가 중복된 경우
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
        InfomationGroupDarkMode("주소", true, "주소검색", {/* 주소 검색 API */}) { addressValue = it }
        InfomationGroupDarkMode("상세주소", false) { detailAddressValue = it }
        InfomationGroupDarkMode("보호자\n이메일", true, "연동하기" ,{
            // TODO: ※ (2024-08-12) 지금은 테스트용으로 보호자 승인 없이 DB에서 바로 받아옴. 진행

            val caregiverUserResponse = UserManager().linkPermission(caregiverEmail)

            /**
             * 에러처리 에러 코드
             *
             * 에러코드 200: 보호자 연동 성공
             * 에러코드 201: API 접속은 성공했으나, 일치하는 보호자 이메일이 존재하지 않음
             * 에러코드 404: 알 수 없는 이유로 이메일 연결을 실패
             * 에러코드 421: 알 수 없는 이유로 이메일 연결을 실패
             * // TODO: 에러처리 추가
             */
            if(caregiverUserResponse.code == 200) {
                sherpaDialog.value.setParm(
                    title = "연동 성공",
                    message =listOf("보호자 연동이 완료되었습니다.")
                )
            }
            else if(caregiverUserResponse.code == 201){
                sherpaDialog.value.setParm(
                    title = "연동 실패",
                    message =listOf("일치하는 보호자 이메일이 없습니다.")
                )
                return@InfomationGroupDarkMode
            }
            else if(caregiverUserResponse.code == 404) {
                sherpaDialog.value.setParm(
                    title = "전송 실패",
                    message =listOf("다시 한번 전송해주세요.")
                )
                return@InfomationGroupDarkMode
            }
            else if(caregiverUserResponse.code == 421) {
                sherpaDialog.value.setParm(
                    title = "연동 실패",
                    message =listOf(caregiverUserResponse.message?:"None")
                )
                return@InfomationGroupDarkMode
            }

            // TODO: 여기에 보호자 연동 허가 로직 구현할 것

            caregiverId = caregiverUserResponse.data!!

            caregiverRelation = "연동 로직 구현 시에는 이거도 같이 반환 받아야 함."
        }) { caregiverEmail = it.trim() }  // 보호자 이메일 텍스트 변경 시 양 끝 공백 제거

        Spacer(modifier = Modifier.height(10.dp))

        /**
         * TOS Area
         *
         * 약관 확인 영역
         */
        ViewTOS(
            tosText = "이용약관에 동의합니다.",
            checked = tosChecked,
            textColor = Color.White,
            onCheckedChange = {
                tosChecked = it
                allChecked = tosChecked && marketingChecked
            }
        )
        ViewTOS(
            tosText = "마케팅에 동의합니다.",
            checked = marketingChecked,
            textColor = Color.White,
            onCheckedChange = {
                marketingChecked = it
                allChecked = tosChecked && marketingChecked
            }
        )
        ViewTOS(
            tosText = "해당 약관에 모두 동의합니다.",
            checked = allChecked,
            textColor = Color.White,
            onCheckedChange = {
                allChecked = it
                tosChecked = it
                marketingChecked = it
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = {
                if(refuseSignup(
                        sherpaDialog,
                        emailValue,
                        passwordValue,
                        confirmPasswordValue,
                        userNameValue,
                        telValue,
                        addressValue,
                        detailAddressValue,
                        caregiverId,
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
                    caregiverId = caregiverId,
                    caregiverRelation = caregiverRelation,
                    createdAt = Timestamp(Calendar.getInstance().timeInMillis),
                    updatedAt = Timestamp(Calendar.getInstance().timeInMillis)
                )

                val userResponse = UserManager().create(createUserRequest)
                if(userResponse.code == 200) navController.navigate(SherpaScreen.Login.name)
                else {
                    sherpaDialog.value.setParm(
                        title = "회원가입 실패",
                        message =listOf("계정 생성을 실패하였습니다")
                    )
                }
            },
            // TODO: 버튼 색상 통일
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

fun refuseSignup(
    sherpaDialog: MutableState<SherpaDialogParm>,
    emailValue: String,
    passwordValue: String,
    confirmPasswordValue: String,
    userNameValue: String,
    telValue: String,
    addressValue: String,
    detailAddressValue: String,
    caregiverId: Int,
    allChecked: Boolean
): Boolean {
    if(caregiverId == -1) {
        sherpaDialog.value.setParm(
            title = "회원가입 실패",
            message =listOf("보호자 연동이 되지 않았습니다")
        )
        return true
    }

    if(passwordValue != confirmPasswordValue){
        sherpaDialog.value.setParm(
            title = "회원가입 실패",
            message =listOf("비밀번호가 다릅니다")
        )
        return true
    }

    //if(!isValidPassword(passwordValue)){
    if(!isValidId(passwordValue)){
        sherpaDialog.value.setParm(
            title = "회원가입 실패",
            message =listOf("비밀번호 서식을 확인해주세요")
        )
        return true
    }

    //if(!isValidEmail(emailValue)){
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
            message =listOf("약관을 모두 동의해주세요")
        )
        return true
    }

    return false
}
