package com.hansung.sherpa.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hansung.sherpa.sherpares.PretendardVariable

val backgroundCardColor = Color(0xFFFFFFFF)
val nomalTextColor = Color.Black
val lightTextColor = Color(0xFF8F8F8F)
val sherpaThemeColor = Color(0xFF34DFD5)

data class SherpaDialogParm(
    var title: String = "",
    var message:List<String> = listOf(),
    var confirmButtonText:String = "",
    var dismissButtonText:String = "",
    var onDismissRequest: () -> Unit = {},
    var onConfirmation: () -> Unit = {}
){
    fun setParm(
        title:String = "",
        message: List<String> = listOf(),
        confirmButtonText: String = "",
        dismissButtonText: String = "",
        onDismissRequest: () -> Unit = {},
        onConfirmation: () -> Unit = {}
    ){
        this.title = title
        this.message = message
        this.confirmButtonText = confirmButtonText
        this.dismissButtonText = dismissButtonText
        this.onDismissRequest = onDismissRequest
        this.onConfirmation = onConfirmation
    }
}
/**
 * 보행자 내비게이션 공용 에러 Dialog
 * 참고: https://developer.android.com/develop/ui/compose/components/dialog?hl=ko
 *
 * @param title 제목
 * @param message 본문 인덱스 하나당 줄 바꿈으로 생각하면 됨
 * @param confirmButtonText 확인 버튼에 나올 메세지
 * @param dissmissButtonText 닫기 버튼에 나올 메세지 Default: ""(None)
 * @param onDismissRequest 닫기 버튼 누를 시 동작할 작업, Dialog 밖을 눌러 닫을 시 수행할 작업
 * @param onConfirmation 확인 버튼 누를 시 동작할 작업
 *
 * @sample ErrorDialogSample
 */
@Composable
fun SherpaDialog(
    title:String,
    message:List<String>,
    confirmButtonText:String,
    dismissButtonText:String = "",
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier.size(320.dp, 220.dp),
            colors = CardColors(backgroundCardColor, backgroundCardColor, backgroundCardColor, backgroundCardColor),
            shape = RoundedCornerShape(12.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = nomalTextColor,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PretendardVariable
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (text in message) {
                        Text(
                            text = text,
                            color = lightTextColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardVariable
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if(dismissButtonText != "") SherpaButton1(
                        dismissButtonText = dismissButtonText,
                        onDismissRequest = onDismissRequest
                    )
                    SherpaButton2(
                        confirmButtonText = confirmButtonText,
                        onConfirmation = onConfirmation
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorDialogSample() {
    // 거절 기능 안넣고 싶은 경우
    SherpaDialog("제목", listOf("내용1", "내용2"), "확인") {
        //TODO: 확인 버튼을 누를 시 실행될 람다 함수
    }

    // 거절 기능 넣고 싶은 경우
    SherpaDialog("제목", listOf("내용1", "내용2"), "확인", "거부", { println("화면 닫을 때 실행할 람다 함수")}) {
        //TODO: 확인 버튼을 누를 시 실행될 람다 함수
    }
}

@Preview
@Composable
fun SherpaDialogPreview(){
    SherpaDialog("로그인 실패", listOf("이메일 혹은 비밀번호를","확인해 주세요"), "확인", "") { println("test") }
}