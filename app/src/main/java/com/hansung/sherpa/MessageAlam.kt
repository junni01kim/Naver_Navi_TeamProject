package com.hansung.sherpa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.hansung.sherpa.FCM.MessageViewModel
import com.hansung.sherpa.ui.common.SherpaDialog

@Composable
fun ExampleAlam(messageViewModel: MessageViewModel) {
    val showDialog by messageViewModel.showDialog.observeAsState(false)
    val title by messageViewModel.title.observeAsState("")
    val body by messageViewModel.body.observeAsState("")

    // 포스트 맨 "알림" 앞에 달고 전송하면 바뀔거임
    if(showDialog) {
        SherpaDialog(title, listOf(body), "확인", onConfirmation = { messageViewModel.onDialogDismiss()})
    }
}