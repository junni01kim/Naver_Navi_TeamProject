package com.hansung.sherpa.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 보행자 내비게이션 공용 에러 Dialog
 * 참고: https://developer.android.com/develop/ui/compose/components/dialog?hl=ko
 *
 * @param title 제목
 * @param message 본문
 * @param confirmButtonText 확인 버튼에 나올 메세지
 * @param dissmissButtonText 닫기 버튼에 나올 메세지 Default: ""(None)
 * @param onDismissRequest 닫기 버튼 누를 시 동작할 작업, Dialog 밖을 눌러 닫을 시 수행할 작업
 * @param onConfirmation 확인 버튼 누를 시 동작할 작업
 *
 * @sample ErrorDialogSample
 */
@Composable
fun ErrorDialog(title:String, message:String, confirmButtonText:String, dissmissButtonText:String = "", onDismissRequest: () -> Unit = {}, onConfirmation: () -> Unit) {
    AlertDialog(
        icon = {},
        onDismissRequest = onDismissRequest,
        title = {Text(title)},
        text = {
            Row(
                modifier = Modifier.width(400.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text(message)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dissmissButtonText)
            }
        }

    )
}

@Composable
fun ErrorDialogSample() {
    // 거절 기능 안넣고 싶은 경우
    ErrorDialog("제목", "내용", "확인") {
        //TODO: 확인 버튼을 누를 시 실행될 람다 함수
    }

    // 거절 기능 넣고 싶은 경우
    ErrorDialog("제목", "내용", "확인", "거부", { println("화면 닫을 때 실행할 람다 함수")}) {
        //TODO: 확인 버튼을 누를 시 실행될 람다 함수
    }
}

@Preview
@Composable
fun ErrorDialogPreview(){
    ErrorDialog("제목", "내용", "확인", "거부") { println("test") }
}